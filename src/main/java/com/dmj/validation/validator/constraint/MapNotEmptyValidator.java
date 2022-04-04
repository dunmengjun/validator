package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.util.Map;

public class MapNotEmptyValidator implements ConstraintValidator<Map<?, ?>> {

  @Override
  public boolean valid(Map<?, ?> value) {
    return !value.isEmpty();
  }
}
