package com.dmj.validation;

import static com.dmj.validation.config.GlobalConfig.getMessage;
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
  void should_return_ok_when_validate_single_bean_given_and_no_group_present() {
    SingleBean bean = new SingleBean();

    ValidationResult actual = BeanValidator.validate(bean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @ParameterizedTest
  @MethodSource("okGenerator")
  void should_return_ok_when_validate_single_bean_given(SingleBean singleBean,
      Class<?> group) {
    ValidationResult actual = BeanValidator.validate(singleBean, group);

    assertEquals(ValidationResult.ok(), actual);
  }

  @ParameterizedTest
  @MethodSource("errorGenerator")
  void should_return_error_when_validate_single_bean_given(SingleBean singleBean,
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
        Arguments.of(new SingleBean(), NotNull.class, "name", getMessage(NotNull.class)),
        Arguments.of(new SingleBean(), NotBlank.class, "name", getMessage(NotBlank.class)),
        Arguments.of(new SingleBean(), NotEmpty.class, "name", getMessage(NotEmpty.class)),
        Arguments.of(new SingleBean("a"), Size.class, "name", getMessage(Size.class, 2, 4)),
        Arguments.of(new SingleBean("a"), Pattern.class, "name", getMessage(Pattern.class, "\\d+")),
        Arguments.of(new SingleBean("a"), Email.class, "name", getMessage(Email.class)),
        Arguments.of(new SingleBean("123"), Digits.class, "name", getMessage(Digits.class, 2, 3)),
        Arguments.of(new SingleBean(false), AssertTrue.class, "flag", getMessage(AssertTrue.class)),
        Arguments.of(new SingleBean(true), AssertFalse.class, "flag",
            getMessage(AssertFalse.class)),
        Arguments.of(new SingleBean(-1), Positive.class, "ints", getMessage(Positive.class)),
        Arguments.of(new SingleBean(0), Positive.class, "ints", getMessage(Positive.class)),
        Arguments.of(new SingleBean(-1), PositiveOrZero.class, "ints",
            getMessage(PositiveOrZero.class)),
        Arguments.of(new SingleBean(1), Negative.class, "ints", getMessage(Negative.class)),
        Arguments.of(new SingleBean(0), Negative.class, "ints", getMessage(Negative.class)),
        Arguments.of(new SingleBean(1), NegativeOrZero.class, "ints",
            getMessage(NegativeOrZero.class)),
        Arguments.of(new SingleBean(99), Min.class, "ints", getMessage(Min.class, 100)),
        Arguments.of(new SingleBean(201), Max.class, "ints", getMessage(Max.class, 200)),
        Arguments.of(new SingleBean(0.32), DecimalMin.class, "decimal",
            getMessage(DecimalMin.class, true, 1.32)),
        Arguments.of(new SingleBean(11.32), DecimalMax.class, "decimal",
            getMessage(DecimalMax.class, true, 10.32))
    );
  }
}
