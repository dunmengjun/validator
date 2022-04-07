package com.dmj.validation.validator.union;

import com.dmj.validation.SelfValidator;
import com.dmj.validation.ValidatorContext;
import com.dmj.validation.validator.UnionValidator;

public class AnyMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    return context.getValidators().stream().anyMatch(SelfValidator::valid);
  }
}
