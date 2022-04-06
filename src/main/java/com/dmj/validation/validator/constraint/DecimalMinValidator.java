package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.DecimalMin;
import com.dmj.validation.validator.ConstraintValidator;

public class DecimalMinValidator implements ConstraintValidator<Object, DecimalMin> {

  @Override
  public boolean valid(Object value, DecimalMin annotation) {
    return false;
  }
}
