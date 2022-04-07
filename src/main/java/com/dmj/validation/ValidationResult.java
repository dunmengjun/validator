package com.dmj.validation;

import static lombok.AccessLevel.PRIVATE;

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

  @AllArgsConstructor(access = PRIVATE)
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class UnionResult {

    private String message;
    private List<FieldResult> fieldResults;

    public static UnionResult from(String path, String message) {
      return new UnionResult(null, Lists.of(FieldResult.from(path, message)));
    }

    public static UnionResult from(List<FieldResult> fieldResults, String message) {
      return new UnionResult(message, fieldResults);
    }
  }

  public static ValidationResult ok() {
    return new ValidationResult(Lists.of());
  }

  public static ValidationResult error(UnionResult... results) {
    return error(Lists.of(results));
  }

  public static ValidationResult error(List<UnionResult> unionResults) {
    return new ValidationResult(unionResults);
  }

  @AllArgsConstructor(access = PRIVATE)
  @Getter
  @ToString
  @EqualsAndHashCode
  public static class FieldResult {

    private String path;
    private String message;

    public static FieldResult from(String path, String message) {
      return new FieldResult(path, message);
    }
  }
}
