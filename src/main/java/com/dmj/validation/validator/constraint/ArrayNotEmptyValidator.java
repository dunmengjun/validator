package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;

public class ArrayNotEmptyValidator implements ConstraintValidator<Object[]> {

  @Override
  public boolean valid(Object[] value) {
    return value.length != 0;
  }
}
