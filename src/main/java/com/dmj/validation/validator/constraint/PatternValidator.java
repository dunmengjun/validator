package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.validator.ConstraintValidator;

public class PatternValidator implements ConstraintValidator<CharSequence, Pattern> {

  @Override
  public boolean valid(CharSequence value, Pattern pattern) {
    return java.util.regex.Pattern.matches(pattern.regexp(), value);
  }
}
