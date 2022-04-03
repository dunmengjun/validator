package com.dmj.validation.validator;

import static com.dmj.validation.validator.FieldValidator.NO_VALID;
import static com.dmj.validation.validator.FieldValidator.VALID;

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

  public Set<String> getFields() {
    return validatorMap.keySet();
  }

  @SuppressWarnings("unchecked")
  public <T> boolean valid(String name, ConstraintValidator<T> validator) {
    return Optional.ofNullable(validatorMap.get(name)).map(fieldValidator -> {
      boolean valid = validator.valid((T) fieldValidator.getValue());
      fieldValidator.setStatus(valid ? VALID : NO_VALID);
      return valid;
    }).orElse(false);
  }
}
