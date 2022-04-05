package com.dmj.validation;

import com.dmj.validation.FieldValidator.TypedConstraintValidator;
import com.dmj.validation.ValidationBeanLayout.FieldPath;
import com.dmj.validation.ValidationBeanLayout.ValidationBean;
import com.dmj.validation.ValidationBeanLayout.ValidationField;
import com.dmj.validation.ValidationBeanLayout.ValidationUnion;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.exception.ReflectionException;
import com.dmj.validation.exception.UnknownException;
import com.dmj.validation.utils.Maps;
import com.dmj.validation.validator.UnionValidator;
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
    ValidationBean validationBean = ValidationBeanLayout.get(bean, group)
        .orElseThrow(UnknownException::new);
    return validate(validationBean, bean);
  }

  public static ValidationResult validate(ValidationBean validationBean, Object bean) {
    ConfigurationValue configuration = validationBean.getConfiguration();
    UnionValidator validator = createValidator(configuration.getValidatedBy());
    Map<String, PartValidator> validatorMap = extendValidatorMap(validationBean, bean);
    ValidatorContext context = new ValidatorContext(toSelfValidator(validatorMap));
    validator.valid(context);
    return new ValidationResult(context.getResults());
  }

  private static Map<String, SelfValidator> toSelfValidator(Map<String, PartValidator> map) {
    return map.entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }

  private static Map<String, PartValidator> extendValidatorMap(ValidationBean validationBean,
      Object bean) {
    Map<String, PartValidator> validatorMap = getPartValidatorMap(
        validationBean.getValidationUnionMap(), bean);
    for (Entry<FieldPath, ValidationBean> entry : validationBean.getNeedExtendBeanMap()
        .entrySet()) {
      FieldPath key = entry.getKey();
      ValidationBean validationBean1 = entry.getValue();
      FieldValue fieldValue = key.getFieldValue(bean);
      Class<?> valueType = fieldValue.getValueType();
      Object value1 = fieldValue.getValue();
      if (Collection.class.isAssignableFrom(valueType)) {
        if (value1 == null) {
          Map<String, PartValidator> map = extendValidatorMap(validationBean1, value1);
          validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
        } else {
          Collection<?> value = (Collection<?>) value1;
          for (Object obj : value) {
            Map<String, PartValidator> map = extendValidatorMap(validationBean1, obj);
            validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
          }
        }
      } else {
        Map<String, PartValidator> map = extendValidatorMap(validationBean1, value1);
        validatorMap = Maps.merge(validatorMap, map, PartValidator::add);
      }
    }
    return validatorMap;
  }

  private static Map<String, PartValidator> getPartValidatorMap(
      Map<Integer, ValidationUnion> validationUnionMap, Object bean) {
    return validationUnionMap.entrySet().stream()
        .collect(Collectors.toMap(entry -> String.valueOf(entry.getKey()),
            entry -> toPartValidator(entry.getValue(), bean)));
  }

  private static PartValidator toPartValidator(ValidationUnion validationUnion, Object bean) {
    UnionValue unionValue = validationUnion.getUnionValue();
    List<UnionValidator> validators = unionValue.getValidatedBy().stream()
        .map(BeanValidator::createValidator).collect(Collectors.toList());
    Map<String, SelfValidator> fieldValidatorMap = validationUnion.getFieldMap().values().stream()
        .map(validationField -> toFieldValidator(validationField, bean))
        .collect(Collectors.toMap(FieldValidator::getPath, v -> v));
    return PartValidator.builder().message(unionValue.getMessage()).validators(validators)
        .validatorContext(new ValidatorContext(fieldValidatorMap)).build();
  }

  private static FieldValidator toFieldValidator(ValidationField field, Object bean) {
    List<TypedConstraintValidator> constraintValidators = field.getValidatedBy().stream()
        .map(BeanValidator::createValidator).map(TypedConstraintValidator::new)
        .collect(Collectors.toList());
    FieldValue fieldValue = field.getFieldPath().getFieldValue(bean);
    return FieldValidator.builder()
        .path(fieldValue.getPath())
        .value(fieldValue.getValue())
        .valueType(fieldValue.getValueType())
        .message(field.getMessage())
        .validators(constraintValidators).build();
  }

  @AllArgsConstructor
  @Getter
  static class FieldValue {

    private String path;
    private Object value;
    private Class<?> valueType;
  }

  private static <T> T createValidator(Class<T> validatorClass) {
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

}
