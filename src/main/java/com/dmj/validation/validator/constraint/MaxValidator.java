package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Max;
import com.dmj.validation.validator.ConstraintValidator;

public class MaxValidator implements ConstraintValidator<Number, Max> {

  @Override
  public boolean valid(Number value, Max annotation) {
    long value1 = annotation.value();
    return value.doubleValue() <= value1;
  }
}
