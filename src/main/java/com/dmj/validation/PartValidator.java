package com.dmj.validation;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.UnionValidator;
import java.util.List;
import lombok.Builder;

@Builder
public class PartValidator extends SelfValidator {

  private String message;

  private List<UnionValidator> validators;

  private ValidatorContext validatorContext;

  @Override
  public boolean doValid() {
    return validators.stream().allMatch(validator -> validator.valid(validatorContext));
  }

  @Override
  protected List<UnionResult> getInnerResults() {
    if (StringUtils.isNotBlank(message)) {
      return Lists.of(new UnionResult(Lists.of(validatorContext.getFields()), message));
    }
    return validatorContext.getResults();
  }

  public PartValidator add(PartValidator partValidator) {
    this.validatorContext.add(partValidator.validatorContext);
    return this;
  }
}
