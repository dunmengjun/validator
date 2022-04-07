package com.dmj.validation.config;

import com.dmj.validation.constraint.AssertFalse;
import com.dmj.validation.constraint.AssertTrue;
import com.dmj.validation.constraint.DecimalMax;
import com.dmj.validation.constraint.DecimalMin;
import com.dmj.validation.constraint.Digits;
import com.dmj.validation.constraint.Email;
import com.dmj.validation.constraint.Max;
import com.dmj.validation.constraint.Min;
import com.dmj.validation.constraint.Negative;
import com.dmj.validation.constraint.NegativeOrZero;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.constraint.Null;
import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.constraint.Positive;
import com.dmj.validation.constraint.PositiveOrZero;
import com.dmj.validation.constraint.Size;
import com.dmj.validation.utils.StringUtils;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class GlobalConfig {

  public static NameTranslator nameTranslator = new PojoLikedStyleTranslator();
  public static Map<Class<? extends Annotation>, String> defaultMessageMap;

  static {
    defaultMessageMap = new HashMap<>();
    defaultMessageMap.put(Null.class, "It must be null");
    defaultMessageMap.put(NotNull.class, "It must not be null");
    defaultMessageMap.put(NotBlank.class,
        "It must not be null and must contain at least one non-whitespace character");
    defaultMessageMap.put(NotEmpty.class, "It must not be null nor empty");
    defaultMessageMap.put(Positive.class, "It must be a positive number");
    defaultMessageMap.put(PositiveOrZero.class, "It must be a positive number or 0");
    defaultMessageMap.put(Negative.class, "It must be a negative number");
    defaultMessageMap.put(NegativeOrZero.class, "It must be a negative number or 0");
    defaultMessageMap.put(Pattern.class, "It must match the regular expression {regexp}");
    defaultMessageMap.put(Size.class, "It must be between the specified boundaries[{min}, {max}]");
    defaultMessageMap.put(Min.class, "It must be higher or equal to the minimum {value}");
    defaultMessageMap.put(Max.class, "It must be lower or equal to the maximum {value}");
    defaultMessageMap.put(Email.class, "It must be a well-formed email address. RFC5322");
    defaultMessageMap.put(Digits.class,
        "It must be a number within accepted range[{integer}, {fraction}]");
    defaultMessageMap.put(DecimalMin.class,
        "It must be higher or equal({inclusive}) to the minimum {value}");
    defaultMessageMap.put(DecimalMax.class,
        "It must be lower or equal({inclusive}) to the minimum {value}");
    defaultMessageMap.put(AssertTrue.class, "It must be true");
    defaultMessageMap.put(AssertFalse.class, "It must be false");
  }

  public static <T extends Annotation> String getMessage(Class<T> annotation, Object... args) {
    String message = defaultMessageMap.get(annotation);
    return StringUtils.format(message, args);
  }
}
