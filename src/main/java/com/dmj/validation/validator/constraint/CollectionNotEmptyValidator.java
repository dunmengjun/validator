package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.util.Collection;

public class CollectionNotEmptyValidator implements ConstraintValidator<Collection<?>, Annotation> {

  @Override
  public boolean valid(Collection<?> list, Annotation annotation) {
    return !list.isEmpty();
  }
}
