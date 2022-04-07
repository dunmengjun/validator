package com.dmj.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.ConfigurationBean;
import com.dmj.validation.validator.union.AnyMatch;
import com.dmj.validation.validator.union.FullMatch;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {

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
        UnionResult.from("name", "It must not be null nor empty")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_one_error_when_validate_given_default_group() {
    ConfigurationBean bean = new ConfigurationBean(9);

    ValidationResult actual = BeanValidator.validate(bean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", "It must not be null nor empty")
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
        UnionResult.from("name",
            "It must not be null and must contain at least one non-whitespace character"),
        UnionResult.from("number", "It must be lower or equal to the maximum 20")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_one_error_when_validate_given_full_match_group() {
    ConfigurationBean bean = new ConfigurationBean(14);

    ValidationResult actual = BeanValidator.validate(bean, FullMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", "It must not be null")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_two_error_when_validate_given_full_match_group() {
    ConfigurationBean bean = new ConfigurationBean(9);

    ValidationResult actual = BeanValidator.validate(bean, FullMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", "It must not be null"),
        UnionResult.from("number", "It must be higher or equal to the minimum 10")
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
