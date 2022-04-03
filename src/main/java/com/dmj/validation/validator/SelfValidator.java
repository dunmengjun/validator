package com.dmj.validation.validator;

import com.dmj.validation.ValidationResult.UnionResult;
import java.util.List;

public abstract class SelfValidator {

  public abstract boolean valid();

  protected abstract List<UnionResult> getResults();
}
