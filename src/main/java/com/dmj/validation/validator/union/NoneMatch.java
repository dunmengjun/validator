package com.dmj.validation.validator.union;

import com.dmj.validation.SelfValidator;
import com.dmj.validation.ValidatorContext;
import com.dmj.validation.validator.UnionValidator;

/**
 * 只要有一个内部校验器成功，该校验器就失败
 */
public class NoneMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    return context.getValidators().stream().noneMatch(SelfValidator::valid);
  }
}
