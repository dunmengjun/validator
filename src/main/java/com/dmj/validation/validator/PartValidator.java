package com.dmj.validation.validator;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class PartValidator extends SelfValidator {

  private String message;

  private List<UnionValidator> validators;

  private ValidatorContext validatorContext;

  @Override
  public boolean valid() {
    return validators.stream().allMatch(validator -> validator.valid(validatorContext));
  }

  @Override
  public List<UnionResult> getResults() {
    if (StringUtils.isNotBlank(message)) {
      return Lists.of(new UnionResult(Lists.of(validatorContext.getFields()), message));
    }
    return validatorContext.validatorMap.values().stream()
        .map(FieldValidator::getResults)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
