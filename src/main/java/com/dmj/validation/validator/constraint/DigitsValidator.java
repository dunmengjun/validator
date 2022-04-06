package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Digits;
import com.dmj.validation.validator.ConstraintValidator;

public class DigitsValidator implements ConstraintValidator<Object, Digits> {

  @Override
  public boolean valid(Object value, Digits annotation) {
    return false;
  }
}
