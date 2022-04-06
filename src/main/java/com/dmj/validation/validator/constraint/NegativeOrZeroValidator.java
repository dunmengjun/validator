package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;

public class NegativeOrZeroValidator implements ConstraintValidator<Number, Annotation> {

  @Override
  public boolean valid(Number value, Annotation annotation) {
    return value.doubleValue() <= 0d;
  }
}
