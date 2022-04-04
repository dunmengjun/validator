/*
 * Bean Validation API
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.dmj.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.constraint.NotBlank.List;
import com.dmj.validation.validator.constraint.NotBlankValidator;
import com.dmj.validation.validator.constraint.NotNullValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must not be {@code null} and must contain at least one non-whitespace
 * character. Accepts {@code CharSequence}.
 *
 * @author Hardy Ferentschik
 * @see Character#isWhitespace(char)
 * @since 2.0
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@Constraint(validatedBy = {
    NotNullValidator.class,
    NotBlankValidator.class
})
public @interface NotBlank {

  String message() default "{com.dmj.validation.constraint.NotBlank}";

  Class<?>[] groups() default {Default.class};

  Class<?>[] unions() default {};

  /**
   * Defines several {@code @NotBlank} constraints on the same element.
   *
   * @see NotBlank
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    NotBlank[] value();
  }
}
