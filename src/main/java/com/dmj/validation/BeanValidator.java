package com.dmj.validation;

import com.dmj.validation.FieldValidator.TypedConstraintValidator;
import com.dmj.validation.ValidationBeanLayout.ValidationBean;
import com.dmj.validation.ValidationBeanLayout.ValidationField;
import com.dmj.validation.ValidationBeanLayout.ValidationUnion;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.exception.ReflectionException;
import com.dmj.validation.utils.Maps;
import com.dmj.validation.utils.ReflectionUtils;
import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.UnionValidator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BeanValidator {

  public static ValidationResult validate(Object bean) {
    return validate(bean, Default.class);
  }

  public static ValidationResult validate(Object bean, Class<?> group) {
    return ValidationBeanLayout.get(bean, group).map(b -> validate(b, bean))
        .orElse(ValidationResult.ok());
  }

  public static ValidationResult validate(ValidationBean validationBean, Object bean) {
    ConfigurationValue configuration = validationBean.getConfiguration();
    UnionValidator validator = createValidator(configuration.getValidatedBy());
    Map<String, PartValidator> validatorMap = extendValidatorMap(validationBean,
        PathBean.from(bean));
    List<SelfValidator> selfValidators = toSelfValidator(validatorMap);
    if (validator.valid(new ValidatorContext(selfValidators))) {
      return ValidationResult.ok();
    }
    List<UnionResult> unionResults = selfValidators.stream()
        .flatMap(selfValidator -> selfValidator.getResults().stream()).collect(Collectors.toList());
    return ValidationResult.error(unionResults);
  }

  private static List<SelfValidator> toSelfValidator(Map<String, PartValidator> map) {
    return map.values().stream()
        .filter(PartValidator::isActive)
        .map(validator -> (SelfValidator) validator)
        .collect(Collectors.toList());
  }

  private static Map<String, PartValidator> extendValidatorMap(ValidationBean validationBean,
      PathBean bean) {
    Map<String, PartValidator> validatorMap = getPartValidatorMap(
        validationBean.getValidationUnionMap(), bean);
    for (Entry<Field, ValidationBean> entry : validationBean.getNeedExtendBeanMap().entrySet()) {
      Field field = entry.getKey();
      ValidationBean validationBean1 = entry.getValue();
      Object object = ReflectionUtils.getValue(field, bean.getBean());
      Class<?> valueType = field.getType();
      String prefix = StringUtils.join(".", bean.getPath(), field.getName());
      if (Collection.class.isAssignableFrom(valueType)) {
        if (object == null) {
          PathBean from = PathBean.from(String.format("%s[]", prefix), null);
          Map<String, PartValidator> map = extendValidatorMap(validationBean1, from);
          validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
        } else {
          Collection<?> value = (Collection<?>) object;
          int index = 0;
          for (Object obj : value) {
            PathBean from = PathBean.from(String.format("%s[%s]", prefix, index), obj);
            Map<String, PartValidator> map = extendValidatorMap(validationBean1, from);
            validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
            index++;
          }
        }
      } else {
        PathBean value1 = PathBean.from(prefix, object);
        Map<String, PartValidator> map = extendValidatorMap(validationBean1, value1);
        validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
      }
    }
    return validatorMap;
  }

  private static Map<String, PartValidator> getPartValidatorMap(
      Map<Integer, ValidationUnion> validationUnionMap, PathBean bean) {
    return validationUnionMap.entrySet().stream().collect(
        Collectors.toMap(entry -> String.valueOf(entry.getKey()),
            entry -> toPartValidator(entry.getKey(), entry.getValue(), bean)));
  }

  private static PartValidator toPartValidator(Integer union, ValidationUnion validationUnion,
      PathBean bean) {
    UnionValue unionValue = validationUnion.getUnionValue();
    boolean isActive = unionValue.isActive();
    List<ValidationField> fields = validationUnion.getFields();
    if (!isActive) {
      isActive = fields.stream().allMatch(field -> union.equals(field.getField().hashCode()));
    }
    List<UnionValidator> validators = unionValue.getValidatedBy().stream()
        .map(BeanValidator::createValidator)
        .collect(Collectors.toList());
    List<SelfValidator> selfValidators = fields.stream()
        .map(validationField -> toFieldValidator(validationField, bean))
        .map(fieldValidator -> (SelfValidator) fieldValidator)
        .collect(Collectors.toList());
    return PartValidator.builder().isActive(isActive).message(unionValue.getMessage())
        .validators(validators).selfValidators(selfValidators).build();
  }

  private static FieldValidator toFieldValidator(ValidationField field, PathBean bean) {
    List<TypedConstraintValidator> constraintValidators = field.getValidatedBy().stream()
        .map(BeanValidator::createValidator).map(TypedConstraintValidator::new)
        .collect(Collectors.toList());
    Field field1 = field.getField();
    Object value = null;
    if (bean.getBean() != null) {
      value = ReflectionUtils.getValue(field1, bean.getBean());
    }
    return FieldValidator.builder().path(StringUtils.join(".", bean.getPath(), field1.getName()))
        .value(value).valueType(field1.getType()).message(field.getMessage())
        .annotation(field.getAnnotation()).isNullValid(field.isNullValid())
        .validators(constraintValidators).build();
  }

  private static <T> T createValidator(Class<T> validatorClass) {
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  @AllArgsConstructor
  @Getter
  static class PathBean {

    private String path;
    private Object bean;

    public static PathBean from(Object bean) {
      return new PathBean("", bean);
    }

    public static PathBean from(String path, Object bean) {
      return new PathBean(path, bean);
    }
  }
}
