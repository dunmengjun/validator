package com.dmj.validation;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import java.util.List;

public abstract class SelfValidator {

  protected static int VALID = 1;
  protected static int INVALID = -1;

  private int status;

  public boolean valid() {
    boolean flag = doValid();
    this.status = flag ? VALID : INVALID;
    return flag;
  }

  public abstract boolean doValid();

  protected List<UnionResult> getResults() {
    if (status == INVALID) {
      return getInnerResults();
    }
    return Lists.of();
  }

  protected abstract List<UnionResult> getInnerResults();

  protected void setStatus(int status) {
    this.status = status;
  }
}
