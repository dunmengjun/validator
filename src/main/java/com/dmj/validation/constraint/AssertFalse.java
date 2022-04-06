package com.dmj.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.constraint.AssertFalse.List;
import com.dmj.validation.validator.constraint.AssertFalseValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must be false. Supported types are {@code boolean} and {@code Boolean}.
 * <p>
 * {@code null} elements are considered valid.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = {AssertFalseValidator.class})
public @interface AssertFalse {

  String message() default "{com.dmj.validation.constraint.AssertFalse}";

  Class<?>[] groups() default {};

  Class<?>[] unions() default {};

  /**
   * Defines several {@link AssertFalse} annotations on the same element.
   *
   * @see AssertFalse
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    AssertFalse[] value();
  }
}
