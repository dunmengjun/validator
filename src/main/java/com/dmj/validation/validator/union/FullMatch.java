package com.dmj.validation.validator.union;

import com.dmj.validation.SelfValidator;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.ValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全部的内部校验器跑一遍, 不管成功还是失败。最后有失败的就判定该校验器失败
 */
public class FullMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    List<Boolean> collect = context.getValidators().parallelStream()
        .map(SelfValidator::valid)
        .collect(Collectors.toList());
    return collect.stream().allMatch(b -> b);
  }
}
