package com.dmj.validation;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public abstract class SelfValidator {

  protected static int VALID = 1;
  protected static int INVALID = -1;

  @Getter
  @Setter
  private int status;

  public boolean valid() {
    boolean flag = doValid();
    this.status = flag ? VALID : INVALID;
    return flag;
  }

  public abstract boolean doValid();

  protected List<UnionResult> getResults() {
    if (isValid()) {
      return Lists.of();
    }
    return getInnerResults();
  }

  protected abstract List<UnionResult> getInnerResults();

  public boolean isValid() {
    return status == VALID;
  }
}
