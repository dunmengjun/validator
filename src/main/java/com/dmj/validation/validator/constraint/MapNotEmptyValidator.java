package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.util.Map;

public class MapNotEmptyValidator implements ConstraintValidator<Map<?, ?>, Annotation> {

  @Override
  public boolean valid(Map<?, ?> value, Annotation annotation) {
    return !value.isEmpty();
  }
}
