package com.dmj.validation;

import com.dmj.validation.ValidationResult.UnionResult;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidatorContext {

  protected Map<String, SelfValidator> validatorMap;

  public boolean valid(String name) {
    return Optional.ofNullable(validatorMap.get(name)).map(SelfValidator::valid).orElse(false);
  }

  public Set<String> getFields() {
    return validatorMap.keySet();
  }

  protected List<UnionResult> getResults() {
    return validatorMap.values().stream()
        .map(SelfValidator::getResults)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
