package com.dmj.validation.validator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidatorContext {

  protected Map<String, FieldValidator> validatorMap;

  public boolean valid(String name) {
    return Optional.ofNullable(validatorMap.get(name)).map(FieldValidator::valid).orElse(false);
  }

  public boolean validAll() {
    return validatorMap.values().stream().allMatch(FieldValidator::valid);
  }

  public Set<String> getFields() {
    return validatorMap.keySet();
  }

  public boolean valid(String name, ConstraintValidator<Object> validator) {
    return Optional.ofNullable(validatorMap.get(name)).map(fieldValidator -> {
      boolean valid = validator.valid(fieldValidator.getValue());
      fieldValidator.setStatus(valid ? 1 : -1);
      return valid;
    }).orElse(false);
  }
}
