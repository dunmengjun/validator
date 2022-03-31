package com.dmj.validation;

import com.dmj.validation.constraint.Union;
import com.dmj.validation.validator.UnionValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnionValue {

  private String message;

  private Class<? extends UnionValidator>[] validatedBy;

  public static UnionValue from(Union union) {
    return new UnionValue(union.message(), union.validatedBy());
  }
}
