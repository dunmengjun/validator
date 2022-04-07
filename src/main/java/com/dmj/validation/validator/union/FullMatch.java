package com.dmj.validation.validator.union;

import com.dmj.validation.SelfValidator;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.ValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class FullMatch implements UnionValidator {

  @Override
  public boolean valid(ValidatorContext context) {
    List<Boolean> collect = context.getValidators().stream()
        .map(SelfValidator::valid)
        .collect(Collectors.toList());
    return collect.stream().allMatch(b -> b);
  }
}
