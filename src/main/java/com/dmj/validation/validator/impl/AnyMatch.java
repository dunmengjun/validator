package com.dmj.validation.validator.impl;

import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.UnionValidatorContext;

public class AnyMatch implements UnionValidator {

  @Override
  public boolean isValid(UnionValidatorContext context) {
    return false;
  }
}
