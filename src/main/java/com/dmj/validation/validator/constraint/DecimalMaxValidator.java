package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.DecimalMax;
import com.dmj.validation.validator.ConstraintValidator;

public class DecimalMaxValidator implements ConstraintValidator<Object, DecimalMax> {

  @Override
  public boolean valid(Object value, DecimalMax annotation) {
    return false;
  }
}
