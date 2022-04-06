package com.dmj.validation;

import com.dmj.validation.utils.Lists;
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

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class UnionResult {

    private List<String> paths;
    private String message;

    public static UnionResult from(String path, String message) {
      return new UnionResult(Lists.of(path), message);
    }
  }

  public static ValidationResult ok() {
    return new ValidationResult(Lists.of());
  }

  public static ValidationResult error(UnionResult... results) {
    return new ValidationResult(Lists.of(results));
  }
}
