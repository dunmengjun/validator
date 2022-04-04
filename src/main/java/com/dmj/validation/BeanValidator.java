package com.dmj.validation;

import com.dmj.validation.FieldValidator.TypedConstraintValidator;
import com.dmj.validation.ValidationBeanLayout.ValidationBean;
import com.dmj.validation.ValidationBeanLayout.ValidationField;
import com.dmj.validation.ValidationBeanLayout.ValidationUnion;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.exception.ReflectionException;
import com.dmj.validation.exception.UnknownException;
import com.dmj.validation.validator.UnionValidator;
import java.util.List;
import java.util.Map;
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
    Map<String, SelfValidator> validatorMap = extendValidatorMap(validationBean, bean);
    ValidatorContext context = new ValidatorContext(validatorMap);
    validator.valid(context);
    return new ValidationResult(context.getResults());
  }

  private static Map<String, SelfValidator> extendValidatorMap(ValidationBean validationBean,
      Object bean) {
    return validationBean.getValidationUnionMap().values()
        .stream()
        .map(validationUnion -> toPartValidator(validationUnion, bean))
        .collect(Collectors.toMap(p -> String.valueOf(p.hashCode()), p -> p));
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
    return FieldValidator.builder().path(fieldValue.getPath()).value(fieldValue.getValue())
        .valueType(fieldValue.getValueType()).message(field.getMessage())
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
