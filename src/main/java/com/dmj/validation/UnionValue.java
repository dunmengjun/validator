package com.dmj.validation;

import com.dmj.validation.constraint.Union;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.union.AllMatch;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UnionValue {

  private boolean isActive;

  private String message;

  private List<Class<? extends UnionValidator>> validatedBy;

  public static UnionValue from(Union union) {
    String message = union.message();
    if (StringUtils.isBlank(message)) {
      message = null;
    }
    return new UnionValue(true, message, Lists.of(union.validatedBy()));
  }

  public static UnionValue empty() {
    return new UnionValue(false, null, Lists.of(AllMatch.class));
  }
}
