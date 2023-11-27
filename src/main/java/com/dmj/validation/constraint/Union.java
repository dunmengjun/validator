package com.dmj.validation.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.constraint.Union.List;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.union.AllMatch;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface Union {

  String message() default "";

  Class<?>[] groups() default Default.class;

  Class<?>[] unions() default {};

  Class<? extends UnionValidator>[] validatedBy() default AllMatch.class;


  /**
   * Defines several {@link Union} annotations on the same element.
   *
   * @see Union
   */
  @Target({TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Union[] value();
  }
}
