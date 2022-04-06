package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<CharSequence, Annotation> {

  private static final Pattern pattern = Pattern.compile(
      "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

  @Override
  public boolean valid(CharSequence value, Annotation annotation) {
    return pattern.matcher(value).find();
  }
}
