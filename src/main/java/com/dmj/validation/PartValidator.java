package com.dmj.validation;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.UnionValidator;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PartValidator extends SelfValidator {

  @Getter
  private boolean isActive;

  private String message;

  private List<UnionValidator> validators;

  protected List<SelfValidator> selfValidators;

  @Override
  public boolean doValid() {
    ValidatorContext validatorContext = new ValidatorContext(selfValidators);
    boolean flag = validators.stream()
        .allMatch(validator -> validator.valid(validatorContext));
    if (StringUtils.isNotBlank(validatorContext.getErrorMessage())) {
      this.message = validatorContext.getErrorMessage();
    }
    return flag;
  }

  @Override
  protected List<UnionResult> getInnerResults() {
    List<FieldResult> fieldResults = selfValidators.stream()
        .map(SelfValidator::getResults)
        .flatMap(Collection::stream)
        .flatMap(unionResult -> unionResult.getFieldResults().stream())
        .collect(Collectors.toList());
    return Lists.of(UnionResult.from(message, fieldResults));
  }

  @Override
  protected String getPath() {
    return selfValidators.stream()
        .map(SelfValidator::getPath)
        .collect(Collectors.joining(","));
  }

  @Override
  protected String getFieldPath() {
    return null;
  }

  @Override
  protected Object getValue() {
    return null;
  }

  public PartValidator add(PartValidator partValidator) {
    if (!this.isActive) {
      this.isActive = partValidator.isActive;
    }
    this.selfValidators.addAll(partValidator.selfValidators);
    return this;
  }
}
