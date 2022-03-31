package com.dmj.validation.constraint;

import com.dmj.validation.validator.UnionValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Union {

  String message() default "{javax.validation.constraints.Size.message}";

  Class<?>[] unions() default {};

  Class<? extends UnionValidator>[] validatedBy();

  Class<?>[] groups() default {Default.class};
}
