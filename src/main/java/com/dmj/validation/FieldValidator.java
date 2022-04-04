package com.dmj.validation;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.exception.NotValidatorException;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.validator.ConstraintValidator;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
public class FieldValidator extends SelfValidator {

  @Getter
  private String path;

  @Getter
  private String message;

  private Class<?> valueType;

  @Getter
  private Object value;

  private List<TypedConstraintValidator> validators;

  @Override
  public boolean doValid() {
    return validators.stream()
        .filter(v -> v.type.isAssignableFrom(valueType) || Object.class.equals(v.type))
        .allMatch(validator -> validator.valid(value));
  }

  @Override
  protected List<UnionResult> getInnerResults() {
    return Lists.of(new UnionResult(Lists.of(path), message));
  }

  public static class TypedConstraintValidator implements ConstraintValidator<Object> {

    private final Class<?> type;
    @SuppressWarnings("rawtypes")
    private final ConstraintValidator validator;

    public TypedConstraintValidator(ConstraintValidator<?> validator) {
      ParameterizedType parameterizedType = Arrays.stream(
              validator.getClass().getGenericInterfaces())
          .map(t -> (ParameterizedType) t)
          .filter(t -> t.getRawType().equals(ConstraintValidator.class))
          .findAny()
          .orElseThrow(NotValidatorException::new);
      this.type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
      this.validator = validator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean valid(Object value) {
      if (value != null && !value.getClass().equals(type)) {
        return true;
      }
      return validator.valid(value);
    }
  }
}
