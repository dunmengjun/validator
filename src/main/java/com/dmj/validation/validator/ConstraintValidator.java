package com.dmj.validation.validator;

@FunctionalInterface
public interface ConstraintValidator<T> {

  boolean valid(T value);
}
