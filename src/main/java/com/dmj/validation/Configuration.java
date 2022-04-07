package com.dmj.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.Configuration.List;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.union.AllMatch;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface Configuration {

  Class<? extends UnionValidator> validatedBy() default AllMatch.class;

  Class<?>[] groups() default {Default.class};

  @Target({TYPE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Configuration[] value();
  }
}
