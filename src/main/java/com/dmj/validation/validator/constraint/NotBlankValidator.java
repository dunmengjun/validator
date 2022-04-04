package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;

public class NotBlankValidator implements ConstraintValidator<CharSequence> {

  @Override
  public boolean valid(CharSequence value) {
    return value.length() != 0;
  }
}
