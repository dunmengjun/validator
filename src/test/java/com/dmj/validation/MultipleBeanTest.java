package com.dmj.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.MultipleBean;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.Size;
import com.dmj.validation.validator.union.AnyMatch;
import org.junit.jupiter.api.Test;

public class MultipleBeanTest {

  @Test
  void should_return_ok_when_validate_given_default_group() {
    MultipleBean multipleBean = new MultipleBean();
    multipleBean.setName("123");

    ValidationResult actual = BeanValidator.validate(multipleBean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_ok_when_validate_given_any_match_group() {
    MultipleBean multipleBean = new MultipleBean();

    ValidationResult actual = BeanValidator.validate(multipleBean, AnyMatch.class);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_tree_error_when_validate_given_any_match_group() {
    MultipleBean multipleBean = new MultipleBean();
    multipleBean.setThirdName("");

    ValidationResult actual = BeanValidator.validate(multipleBean, AnyMatch.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("thirdName",
                "It must not be null and must contain at least one non-whitespace character"),
            FieldResult.from(
                "thirdName", "It must be between the specified boundaries[2, 4]"),
            FieldResult.from("thirdName", "It must match the regular expression \\d+")
        )
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_not_blank_error_when_validate_given_default_group() {
    MultipleBean multipleBean = new MultipleBean();

    ValidationResult actual = BeanValidator.validate(multipleBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name",
            "It must not be null and must contain at least one non-whitespace character")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_size_error_when_validate_given_default_group() {
    MultipleBean multipleBean = new MultipleBean();
    multipleBean.setName("1");

    ValidationResult actual = BeanValidator.validate(multipleBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name",
            "It must be between the specified boundaries[2, 4]")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_two_error_when_validate_given_default_group() {
    MultipleBean multipleBean = new MultipleBean();
    multipleBean.setName("a");

    ValidationResult actual = BeanValidator.validate(multipleBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("name",
                "It must be between the specified boundaries[2, 4]"),
            FieldResult.from("name", "It must match the regular expression \\d+")
        )
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_size_error_when_validate_given_not_empty_group() {
    MultipleBean multipleBean = new MultipleBean();

    ValidationResult actual = BeanValidator.validate(multipleBean, NotEmpty.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("fullName",
            "It must not be null nor empty")
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_size_error_when_validate_given_size_group() {
    MultipleBean multipleBean = new MultipleBean();
    multipleBean.setSecondName("a");

    ValidationResult actual = BeanValidator.validate(multipleBean, Size.class);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("secondName",
                "It must be between the specified boundaries[2, 6]"),
            FieldResult.from("secondName",
                "It must be between the specified boundaries[3, 7]")
        )
    );
    assertEquals(expected, actual);
  }
}
