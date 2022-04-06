package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Min;
import com.dmj.validation.validator.ConstraintValidator;

public class MinValidator implements ConstraintValidator<Number, Min> {

  @Override
  public boolean valid(Number value, Min annotation) {
    long value1 = annotation.value();
    return value.doubleValue() >= value1;
  }
}
