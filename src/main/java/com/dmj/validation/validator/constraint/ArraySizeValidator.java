package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Size;
import com.dmj.validation.validator.ConstraintValidator;

public class ArraySizeValidator implements ConstraintValidator<Object[], Size> {

  @Override
  public boolean valid(Object[] value, Size annotation) {
    int min = annotation.min();
    int max = annotation.max();
    int length = value.length;
    return length >= min && length < max;
  }
}
