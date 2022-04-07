package com.dmj.validation;

import static com.dmj.validation.utils.ReflectionUtils.invokeMethod;
import static com.dmj.validation.utils.ReflectionUtils.isAssignableFrom;
import static com.dmj.validation.utils.ReflectionUtils.isClassEquals;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.config.GlobalConfig;
import com.dmj.validation.exception.NotValidatorException;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
public class FieldValidator extends SelfValidator {

  @Getter
  private String path;

  @Getter
  private String message;

  private Class<?> valueType;

  private Annotation annotation;

  private boolean isNullValid;

  @Getter
  private Object value;

  private List<TypedConstraintValidator> validators;

  private static final Pattern pattern = Pattern.compile("\\{(.*?)}");

  @Override
  public boolean doValid() {
    if (isNullValid && value == null) {
      return true;
    }
    if (!isNullValid && value == null) {
      return false;
    }
    return validators.stream()
        .filter(v -> isClassEquals(valueType, v.genericValueType)
            || isAssignableFrom(v.genericValueType, valueType)
            || Object.class.equals(v.genericValueType))
        .filter(v -> v.genericAnnotationType.equals(annotation.annotationType())
            || v.genericAnnotationType.equals(Annotation.class))
        .allMatch(validator -> validator.valid(value, annotation));
  }

  @Override
  protected List<UnionResult> getInnerResults() {
    String localMessage = message;
    if (StringUtils.isBlank(localMessage)) {
      String globalMessage = GlobalConfig.defaultMessageMap.get(annotation.annotationType());
      if (StringUtils.isNotBlank(globalMessage)) {
        localMessage = globalMessage;
      }
    }
    return Lists.of(UnionResult.from(path, format(localMessage, annotation)));
  }

  private String format(String source, Annotation annotation) {
    if (annotation == null) {
      return source;
    }
    return StringUtils.format(source, (index, key) -> invokeMethod(annotation, key));
  }

  public static class TypedConstraintValidator implements ConstraintValidator<Object, Annotation> {

    private final Class<?> genericValueType;
    private final Class<?> genericAnnotationType;
    @SuppressWarnings("rawtypes")
    private final ConstraintValidator validator;

    public TypedConstraintValidator(ConstraintValidator<?, ?> validator) {
      ParameterizedType parameterizedType = Arrays.stream(
              validator.getClass().getGenericInterfaces()).map(t -> (ParameterizedType) t)
          .filter(t -> t.getRawType().equals(ConstraintValidator.class)).findAny()
          .orElseThrow(NotValidatorException::new);
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
      Type argument = actualTypeArguments[0];
      if (argument instanceof ParameterizedType) {
        ParameterizedType argument1 = (ParameterizedType) argument;
        this.genericValueType = (Class<?>) argument1.getRawType();
      } else {
        this.genericValueType = (Class<?>) argument;
      }
      argument = actualTypeArguments[1];
      this.genericAnnotationType = (Class<?>) argument;
      this.validator = validator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean valid(Object value, Annotation annotation) {
      return validator.valid(value, annotation);
    }
  }
}
