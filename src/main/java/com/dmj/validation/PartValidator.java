package com.dmj.validation;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.validator.UnionValidator;
import java.util.List;
import java.util.stream.Collectors;
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
    List<FieldResult> fieldResults = validatorContext.getResults().stream()
        .flatMap(unionResult -> unionResult.getFieldResults().stream())
        .collect(Collectors.toList());
    return Lists.of(UnionResult.from(fieldResults, message));
  }

  public PartValidator add(PartValidator partValidator) {
    this.validatorContext.add(partValidator.validatorContext);
    return this;
  }
}
