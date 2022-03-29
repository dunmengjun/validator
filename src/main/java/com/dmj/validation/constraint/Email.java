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

import com.dmj.validation.constraint.Email.List;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The string has to be a well-formed email address. Exact semantics of what makes up a valid email
 * address are left to Bean Validation providers. Accepts {@code CharSequence}.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @since 2.0
 */
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
public @interface Email {

  String message() default "{javax.validation.constraints.Email.message}";

  Class<?>[] groups() default {};

  /**
   * @return an additional regular expression the annotated element must match. The default is any
   * string ('.*')
   */
  String regexp() default ".*";

  /**
   * @return used in combination with {@link #regexp()} in order to specify a regular expression
   * option
   */
  Pattern.Flag[] flags() default {};

  /**
   * Defines several {@code @Email} constraints on the same element.
   *
   * @see Email
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
  @Retention(RUNTIME)
  @Documented
  @interface List {

    Email[] value();
  }
}
