package com.dmj.validation;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.validator.UnionValidator;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class PartValidator extends SelfValidator {

  private String message;

  private List<UnionValidator> validators;

  protected List<SelfValidator> selfValidators;

  @Override
  public boolean doValid() {
    return validators.stream()
        .allMatch(validator -> validator.valid(new ValidatorContext(selfValidators)));
  }

  @Override
  protected List<UnionResult> getInnerResults() {
    List<FieldResult> fieldResults = selfValidators.stream()
        .map(SelfValidator::getResults)
        .flatMap(Collection::stream)
        .flatMap(unionResult -> unionResult.getFieldResults().stream())
        .collect(Collectors.toList());
    return Lists.of(UnionResult.from(fieldResults, message));
  }

  @Override
  protected String getPath() {
    return selfValidators.stream()
        .map(SelfValidator::getPath)
        .collect(Collectors.joining(","));
  }

  public PartValidator add(PartValidator partValidator) {
    this.selfValidators.addAll(partValidator.selfValidators);
    return this;
  }
}
