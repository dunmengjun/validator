package com.dmj.validation.validator;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public class PartValidator {

  private String message;

  private List<UnionValidator> validators;

  private ValidatorContext validatorContext;

  public boolean valid() {
    return validators.stream().allMatch(validator -> validator.valid(validatorContext));
  }

  public List<UnionResult> getResult() {
    if (StringUtils.isNotBlank(message)) {
      return Lists.of(new UnionResult(Lists.of(validatorContext.getFields()), message));
    }
    return validatorContext.validatorMap.values().stream()
        .filter(validator -> -1 == validator.getStatus())
        .map(validator -> new UnionResult(Lists.of(validator.getPath()), validator.getMessage()))
        .collect(Collectors.toList());
  }
}
