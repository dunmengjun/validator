package com.dmj.validation.validator.constraint;

import com.dmj.validation.validator.ConstraintValidator;
import java.util.List;

public class ListNotEmptyValidator implements ConstraintValidator<List<?>> {

  @Override
  public boolean valid(List<?> list) {
    return !list.isEmpty();
  }
}
