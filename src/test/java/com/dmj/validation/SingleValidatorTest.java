package com.dmj.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.SingleBean;
import com.dmj.validation.constraint.AssertFalse;
import com.dmj.validation.constraint.AssertTrue;
import com.dmj.validation.constraint.DecimalMax;
import com.dmj.validation.constraint.DecimalMin;
import com.dmj.validation.constraint.Default;
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
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SingleValidatorTest {

  @Test
  public void should_return_ok_when_validate_single_bean_given_and_no_group_present() {
    SingleBean bean = new SingleBean();

    ValidationResult actual = BeanValidator.validate(bean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @ParameterizedTest
  @MethodSource("okGenerator")
  public void should_return_ok_when_validate_single_bean_given(SingleBean singleBean,
      Class<?> group) {
    ValidationResult actual = BeanValidator.validate(singleBean, group);

    assertEquals(ValidationResult.ok(), actual);
  }

  @ParameterizedTest
  @MethodSource("errorGenerator")
  public void should_return_error_when_validate_single_bean_given(SingleBean singleBean,
      Class<?> group, String path, String message) {
    ValidationResult actual = BeanValidator.validate(singleBean, group);

    ValidationResult expected = ValidationResult.error(UnionResult.from(path, message));
    assertEquals(expected, actual);
  }

  static Stream<Arguments> okGenerator() {
    return Stream.of(
        Arguments.of(new SingleBean(), Default.class),
        Arguments.of(new SingleBean(), Null.class),
        Arguments.of(new SingleBean("a"), NotNull.class),
        Arguments.of(new SingleBean("a"), NotBlank.class),
        Arguments.of(new SingleBean("a"), NotEmpty.class),
        Arguments.of(new SingleBean("abc"), Size.class),
        Arguments.of(new SingleBean("123"), Pattern.class),
        Arguments.of(new SingleBean("123@xxx.com"), Email.class),
        Arguments.of(new SingleBean("12.123"), Digits.class),
        Arguments.of(new SingleBean(true), AssertTrue.class),
        Arguments.of(new SingleBean(false), AssertFalse.class),
        Arguments.of(new SingleBean(1), Positive.class),
        Arguments.of(new SingleBean(1), PositiveOrZero.class),
        Arguments.of(new SingleBean(0), PositiveOrZero.class),
        Arguments.of(new SingleBean(-1), Negative.class),
        Arguments.of(new SingleBean(-1), NegativeOrZero.class),
        Arguments.of(new SingleBean(0), NegativeOrZero.class),
        Arguments.of(new SingleBean(101), Min.class),
        Arguments.of(new SingleBean(101), Max.class),
        Arguments.of(new SingleBean(199), Max.class),
        Arguments.of(new SingleBean(2.32), DecimalMin.class),
        Arguments.of(new SingleBean(9.32), DecimalMax.class)
    );
  }

  static Stream<Arguments> errorGenerator() {
    return Stream.of(
        Arguments.of(new SingleBean(), NotNull.class, "name", "It must not be null"),
        Arguments.of(new SingleBean(), NotBlank.class, "name",
            "It must not be null and must contain at least one non-whitespace character"),
        Arguments.of(new SingleBean(), NotEmpty.class, "name", "It must not be null nor empty"),
        Arguments.of(new SingleBean("a"), Size.class, "name",
            "It must be between the specified boundaries[2, 4]"),
        Arguments.of(new SingleBean("a"), Pattern.class, "name",
            "It must match the regular expression \\d+"),
        Arguments.of(new SingleBean("a"), Email.class, "name",
            "It must be a well-formed email address. RFC5322"),
        Arguments.of(new SingleBean("123"), Digits.class, "name",
            "It must be a number within accepted range[2, 3]"),
        Arguments.of(new SingleBean(false), AssertTrue.class, "flag", "It must be true"),
        Arguments.of(new SingleBean(true), AssertFalse.class, "flag", "It must be false"),
        Arguments.of(new SingleBean(-1), Positive.class, "ints", "It must be a positive number"),
        Arguments.of(new SingleBean(0), Positive.class, "ints", "It must be a positive number"),
        Arguments.of(new SingleBean(-1), PositiveOrZero.class, "ints",
            "It must be a positive number or 0"),
        Arguments.of(new SingleBean(1), Negative.class, "ints",
            "It must be a negative number"),
        Arguments.of(new SingleBean(0), Negative.class, "ints",
            "It must be a negative number"),
        Arguments.of(new SingleBean(1), NegativeOrZero.class, "ints",
            "It must be a negative number or 0"),
        Arguments.of(new SingleBean(99), Min.class, "ints",
            "It must be higher or equal to the minimum 100"),
        Arguments.of(new SingleBean(201), Max.class, "ints",
            "It must be lower or equal to the maximum 200"),
        Arguments.of(new SingleBean(0.32), DecimalMin.class, "decimal",
            "It must be higher or equal(true) to the minimum 1.32"),
        Arguments.of(new SingleBean(11.32), DecimalMax.class, "decimal",
            "It must be lower or equal(true) to the minimum 10.32")
    );
  }
}
