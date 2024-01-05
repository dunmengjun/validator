package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Digits;
import com.dmj.validation.validator.ConstraintValidator;
import java.util.regex.Pattern;

public class DigitsCharSequenceValidator implements ConstraintValidator<CharSequence, Digits> {

  private static final Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");

  @Override
  public boolean valid(CharSequence value, Digits annotation) {
    int integer = annotation.integer() > 0 ? annotation.integer() : 1;
    int fraction = Math.max(annotation.fraction(), 0);
    if (value.length() == 1 && value.charAt(0) == '0') {
      return true;
    }
    boolean matches = pattern.matcher(value).matches();
    if (!matches) {
      return false;
    }
    int length = value.length();
    int dotIndex = length;
    for (int i = 0; i < length; i++) {
      if (value.charAt(i) == '.') {
        dotIndex = i;
        break;
      }
    }
    int integerNum = dotIndex;
    int fractionNum = dotIndex < length ? length - dotIndex - 1 : 0;
    return integerNum <= integer && fractionNum <= fraction;
  }
}
