package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;

public class AssertTrueValidator implements ConstraintValidator<Boolean, Annotation> {

  @Override
  public boolean valid(Boolean value, Annotation annotation) {
    return value;
  }
}
