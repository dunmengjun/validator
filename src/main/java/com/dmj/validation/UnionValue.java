package com.dmj.validation;

import com.dmj.validation.constraint.Union;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.union.FullMatch;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnionValue {

  private String message;

  private List<Class<? extends UnionValidator>> validatedBy;

  public static UnionValue from(Union union) {
    return new UnionValue(union.message(), Lists.of(union.validatedBy()));
  }

  public static UnionValue empty() {
    return new UnionValue(null, Lists.of(FullMatch.class));
  }
}
