package com.dmj.validation.validator.union;

import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.ValidatorContext;

public class AllMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    return context.getFields().stream().allMatch(context::valid);
  }
}
