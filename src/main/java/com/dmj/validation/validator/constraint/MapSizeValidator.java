package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.Size;
import com.dmj.validation.validator.ConstraintValidator;
import java.util.Map;

public class MapSizeValidator implements ConstraintValidator<Map<?, ?>, Size> {

  @Override
  public boolean valid(Map<?, ?> value, Size annotation) {
    int size = value.size();
    int max = annotation.max();
    int min = annotation.min();
    return size >= min && size < max;
  }
}
