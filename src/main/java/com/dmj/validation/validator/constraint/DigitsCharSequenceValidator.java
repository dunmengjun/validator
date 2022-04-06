package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Digits;
import com.dmj.validation.validator.ConstraintValidator;
import java.util.regex.Pattern;

public class DigitsCharSequenceValidator implements ConstraintValidator<CharSequence, Digits> {

  @Override
  public boolean valid(CharSequence value, Digits annotation) {
    int integer = annotation.integer() > 0 ? annotation.integer() : 1;
    int fraction = Math.max(annotation.fraction(), 0);
    if (fraction == 0) {
      return Pattern.matches(String.format("\\d{%s}", integer), value);
    } else {
      return Pattern.matches(String.format("\\d{%s}.\\d{%s}", integer, fraction), value);
    }
  }
}
