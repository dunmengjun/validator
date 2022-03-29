package com.dmj.validation.validator;

public interface ConstraintValidator<T> {

  boolean isValid(T value, ConstraintValidatorContext context);
}
