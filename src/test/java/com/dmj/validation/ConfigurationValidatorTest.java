package com.dmj.validation;

import static com.dmj.validation.config.GlobalConfig.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.ConfigurationBean;
import com.dmj.validation.constraint.Max;
import com.dmj.validation.constraint.Min;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.validator.union.AnyMatch;
import com.dmj.validation.validator.union.FullMatch;
import org.junit.jupiter.api.Test;

public class ConfigurationValidatorTest {

  @Test
  void should_return_ok_when_validate_given_default_group() {
    ConfigurationBean bean = new ConfigurationBean("a", 16);

    ValidationResult actual = BeanValidator.validate(bean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_error_when_validate_given_default_group() {
    ConfigurationBean bean = new ConfigurationBean(16);

    ValidationResult actual = BeanValidator.validate(bean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotEmpty.class))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_one_error_when_validate_given_default_group() {
    ConfigurationBean bean = new ConfigurationBean(9);

    ValidationResult actual = BeanValidator.validate(bean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotEmpty.class))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_ok_when_validate_given_any_match_group() {
    ConfigurationBean bean = new ConfigurationBean(14);

    ValidationResult actual = BeanValidator.validate(bean, AnyMatch.class);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_error_when_validate_given_any_match_group() {
    ConfigurationBean bean = new ConfigurationBean(21);

    ValidationResult actual = BeanValidator.validate(bean, AnyMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotBlank.class)),
        UnionResult.from("number", getMessage(Max.class, 20))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_one_error_when_validate_given_full_match_group() {
    ConfigurationBean bean = new ConfigurationBean(14);

    ValidationResult actual = BeanValidator.validate(bean, FullMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotNull.class))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_two_error_when_validate_given_full_match_group() {
    ConfigurationBean bean = new ConfigurationBean(9);

    ValidationResult actual = BeanValidator.validate(bean, FullMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotNull.class)),
        UnionResult.from("number", getMessage(Min.class, 10))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_ok_when_validate_given_full_match_group() {
    ConfigurationBean bean = new ConfigurationBean("", 14);

    ValidationResult actual = BeanValidator.validate(bean, FullMatch.class);

    assertEquals(ValidationResult.ok(), actual);
  }

}
