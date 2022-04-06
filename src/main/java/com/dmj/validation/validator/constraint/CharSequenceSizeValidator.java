package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Size;
import com.dmj.validation.validator.ConstraintValidator;

public class CharSequenceSizeValidator implements ConstraintValidator<CharSequence, Size> {

  @Override
  public boolean valid(CharSequence value, Size size) {
    int min = size.min();
    int max = size.max();
    int length = value.length();
    return length >= min && length < max;
  }
}
