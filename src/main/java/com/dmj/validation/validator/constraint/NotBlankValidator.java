package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;

public class NotBlankValidator implements ConstraintValidator<CharSequence, Annotation> {

  @Override
  public boolean valid(CharSequence value, Annotation annotation) {
    return value.length() != 0;
  }
}
