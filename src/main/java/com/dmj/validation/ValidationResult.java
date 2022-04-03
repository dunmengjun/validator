package com.dmj.validation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ValidationResult {

  List<UnionResult> results;

  public static ValidationResult ok() {
    return new ValidationResult(null);
  }

  public static ValidationResult error(List<UnionResult> results) {
    return new ValidationResult(results);
  }

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class UnionResult {

    private List<String> paths;
    private String message;
  }
}
