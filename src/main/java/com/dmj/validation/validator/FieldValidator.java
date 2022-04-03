package com.dmj.validation.validator;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.exception.NotValidatorException;
import com.dmj.validation.utils.Lists;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class FieldValidator extends SelfValidator {

  protected static int VALID = 1;
  protected static int INVALID = -1;
  protected static int NO_VALID = 0;

  @Getter
  private String path;

  @Getter
  private String message;

  @Getter
  private Object value;

  @Getter
  @Setter
  private int status;

  private List<TypedConstraintValidator> validators;

  @Override
  public boolean valid() {
    boolean flag = validators.stream().allMatch(validator -> validator.valid(value));
    this.status = flag ? VALID : INVALID;
    return flag;
  }

  @Override
  protected List<UnionResult> getResults() {
    if (INVALID != status) {
      return Lists.of();
    }
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
