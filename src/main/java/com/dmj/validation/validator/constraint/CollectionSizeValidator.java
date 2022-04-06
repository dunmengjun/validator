package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Size;
import com.dmj.validation.validator.ConstraintValidator;
import java.util.Collection;

public class CollectionSizeValidator implements ConstraintValidator<Collection<?>, Size> {

  @Override
  public boolean valid(Collection<?> value, Size annotation) {
    int size = value.size();
    int min = annotation.min();
    int max = annotation.max();
    return size >= min && size < max;
  }
}
