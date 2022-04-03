package com.dmj.validation.validator;

public class DefaultUnionValidator implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    return context.validAll();
  }
}
