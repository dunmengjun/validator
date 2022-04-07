package com.dmj.validation;

import java.util.List;

public class ValidatorContext {

  protected List<SelfValidator> selfValidators;

  public ValidatorContext(List<SelfValidator> selfValidators) {
    this.selfValidators = selfValidators;
  }

  public List<SelfValidator> getValidators() {
    return selfValidators;
  }
}
