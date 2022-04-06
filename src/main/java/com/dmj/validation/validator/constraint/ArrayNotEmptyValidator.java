package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;

public class ArrayNotEmptyValidator implements ConstraintValidator<Object[], Annotation> {

  @Override
  public boolean valid(Object[] value, Annotation annotation) {
    return value.length != 0;
  }
}
