package com.dmj.validation;

import com.dmj.validation.ValidationBeanLayout.ValidationBean;
import com.dmj.validation.ValidationBeanLayout.ValidationField;
import com.dmj.validation.ValidationBeanLayout.ValidationUnion;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.exception.ReflectionException;
import com.dmj.validation.validator.FieldValidator;
import com.dmj.validation.validator.FieldValidator.TypedConstraintValidator;
import com.dmj.validation.validator.PartValidator;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.ValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BeanValidator {

  public static ValidationResult validate(Object bean) {
    return validate(bean, Default.class);
  }

  public static ValidationResult validate(Object bean, Class<?> group) {
    ValidationBean validationBean = ValidationBeanLayout.get(bean, group);
    return validate(validationBean, bean);
  }

  public static ValidationResult validate(ValidationBean validationBean, Object bean) {
    ConfigurationValue configuration = validationBean.getConfiguration();
    ValidationType validationType = configuration.getValidationType();
    Stream<PartValidator> partValidatorStream = validationBean.getValidationUnionMap()
        .values().stream()
        .map(validationUnion -> toPartValidator(validationUnion, bean))
        .filter(validator -> !validator.valid());
    if (ValidationType.AllMatch.equals(validationType)) {
      return partValidatorStream
          .findAny()
          .map(validator -> ValidationResult.error(validator.getResults()))
          .orElse(ValidationResult.ok());
    } else {
      List<UnionResult> unionResults = partValidatorStream
          .flatMap(partValidator -> partValidator.getResults().stream())
          .collect(Collectors.toList());
      return new ValidationResult(unionResults);
    }
  }

  private static PartValidator toPartValidator(ValidationUnion validationUnion, Object bean) {
    UnionValue unionValue = validationUnion.getUnionValue();
    List<UnionValidator> validators = unionValue.getValidatedBy().stream()
        .map(BeanValidator::createValidator)
        .collect(Collectors.toList());
    Map<String, FieldValidator> fieldValidatorMap = validationUnion.getFieldMap().values().stream()
        .map(validationField -> toFieldValidator(validationField, bean))
        .collect(Collectors.toMap(FieldValidator::getPath, v -> v));
    return PartValidator.builder()
        .message(unionValue.getMessage())
        .validators(validators)
        .validatorContext(new ValidatorContext(fieldValidatorMap))
        .build();
  }

  private static FieldValidator toFieldValidator(ValidationField field, Object bean) {
    List<TypedConstraintValidator> constraintValidators = field.getValidatedBy()
        .stream()
        .map(BeanValidator::createValidator)
        .map(TypedConstraintValidator::new)
        .collect(Collectors.toList());
    FieldValue fieldValue = field.getFieldPath().getFieldValue(bean);
    return FieldValidator.builder()
        .path(fieldValue.getPath())
        .value(fieldValue.getValue())
        .message(field.getMessage())
        .validators(constraintValidators)
        .build();
  }

  @AllArgsConstructor
  @Getter
  static class FieldValue {

    private String path;
    private Object value;
  }

  private static <T> T createValidator(
      Class<T> validatorClass) {
    try {
      return validatorClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

}
