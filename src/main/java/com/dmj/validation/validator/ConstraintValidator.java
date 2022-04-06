package com.dmj.validation.validator;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface ConstraintValidator<T, R extends Annotation> {

  boolean valid(T value, R annotation);
}
