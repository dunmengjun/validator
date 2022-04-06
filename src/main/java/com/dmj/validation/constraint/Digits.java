package com.dmj.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.constraint.Digits.List;
import com.dmj.validation.validator.constraint.DigitsCharSequenceValidator;
import com.dmj.validation.validator.constraint.DigitsValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must be a number within accepted range Supported types are:
 * <ul>
 *     <li>{@code BigDecimal}</li>
 *     <li>{@code BigInteger}</li>
 *     <li>{@code CharSequence}</li>
 *     <li>{@code byte}, {@code short}, {@code int}, {@code long}, and their respective
 *     wrapper types</li>
 * </ul>
 * <p>
 * {@code null} elements are considered valid.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = {
    DigitsCharSequenceValidator.class,
    DigitsValidator.class
})
public @interface Digits {

  String message() default "{com.dmj.validation.constraint.Digits}";

  Class<?>[] groups() default {};

  Class<?>[] unions() default {};

  /**
   * @return maximum number of integral digits accepted for this number
   */
  int integer();

  /**
   * @return maximum number of fractional digits accepted for this number
   */
  int fraction();

  /**
   * Defines several {@link Digits} annotations on the same element.
   *
   * @see Digits
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Digits[] value();
  }
}
