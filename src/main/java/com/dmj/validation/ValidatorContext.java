package com.dmj.validation;

import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidatorContext {

  private final List<SelfValidator> selfValidators;

  private final Map<String, Object> pathValueMap;

  @Getter
  @Setter
  private String errorMessage;

  public ValidatorContext(List<SelfValidator> selfValidators) {
    this.selfValidators = selfValidators;
    this.pathValueMap = new HashMap<>();
    for (SelfValidator selfValidator : selfValidators) {
      pathValueMap.put(selfValidator.getFieldPath(), selfValidator.getValue());
    }
  }

  public List<SelfValidator> getValidators() {
    return selfValidators;
  }

  @SafeVarargs
  public final <T, R> R getValue(Class<R> targetClass, FieldLambda<T, R>... fieldLambdas) {
    String fieldPath = Arrays.stream(fieldLambdas).map(FieldLambda::getName)
        .collect(Collectors.joining(","));
    Object obj = pathValueMap.get(fieldPath);
    if (Objects.nonNull(obj)) {
      return targetClass.cast(obj);
    }
    return null;
  }
}
