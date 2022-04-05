package com.dmj.validation;

import static com.dmj.validation.SelfValidator.INVALID;
import static com.dmj.validation.SelfValidator.VALID;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.exception.NotFieldValidatorException;
import com.dmj.validation.validator.ConstraintValidator;
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

  public <T, R> boolean valid(FieldLambda<T, R> fieldLambda) {
    return valid(fieldLambda.getName());
  }

  @SuppressWarnings("unchecked")
  public <T> boolean valid(String name, ConstraintValidator<T> validator) {
    SelfValidator selfValidator = validatorMap.get(name);
    if (selfValidator instanceof FieldValidator) {
      FieldValidator fieldValidator = (FieldValidator) selfValidator;
      boolean valid = validator.valid((T) fieldValidator.getValue());
      fieldValidator.setStatus(valid ? VALID : INVALID);
    }
    throw new NotFieldValidatorException();
  }

  public <T, R> boolean valid(FieldLambda<T, R> fieldLambda, ConstraintValidator<R> validator) {
    return valid(fieldLambda.getName(), validator);
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

  protected ValidatorContext add(ValidatorContext context) {
    this.validatorMap.putAll(context.validatorMap);
    return this;
  }
}
