package com.dmj.validation.constraint;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dmj.validation.constraint.NotEmpty.List;
import com.dmj.validation.validator.constraint.ArrayNotEmptyValidator;
import com.dmj.validation.validator.constraint.CollectionNotEmptyValidator;
import com.dmj.validation.validator.constraint.MapNotEmptyValidator;
import com.dmj.validation.validator.constraint.NotBlankValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element must not be {@code null} nor empty. Supported types are:
 * <ul>
 * <li>{@code CharSequence} (length of character sequence is evaluated)</li>
 * <li>{@code Collection} (collection size is evaluated)</li>
 * <li>{@code Map} (map size is evaluated)</li>
 * <li>Array (array length is evaluated)</li>
 * </ul>
 */
@Documented
@Constraint(
    isNullValid = false,
    validatedBy = {
        NotBlankValidator.class,
        CollectionNotEmptyValidator.class,
        ArrayNotEmptyValidator.class,
        MapNotEmptyValidator.class
    })
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface NotEmpty {

  String message() default "";

  Class<?>[] groups() default {};

  Class<?>[] unions() default {};


  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    NotEmpty[] value();
  }
}
