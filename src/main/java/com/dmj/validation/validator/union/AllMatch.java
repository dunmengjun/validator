package com.dmj.validation.validator.union;

import com.dmj.validation.SelfValidator;
import com.dmj.validation.ValidatorContext;
import com.dmj.validation.validator.UnionValidator;

/**
 * 只要有一个内部校验器失败，该校验器就失败
 */
public class AllMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    return context.getValidators().stream().allMatch(SelfValidator::valid);
  }
}
