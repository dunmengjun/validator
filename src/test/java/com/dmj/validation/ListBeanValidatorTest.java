package com.dmj.validation;

import static com.dmj.validation.config.GlobalConfig.getMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dmj.validation.ValidationResult.FieldResult;
import com.dmj.validation.ValidationResult.UnionResult;
import com.dmj.validation.bean.InnerBean;
import com.dmj.validation.bean.ListBean;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.utils.Lists;
import org.junit.jupiter.api.Test;

public class ListBeanValidatorTest {

  @Test
  void should_return_ok_when_validate_given_default_group_and_valid_bean() {
    ListBean listBean = new ListBean();
    listBean.setName("xx");
    listBean.setInnerBeans(Lists.of(new InnerBean("xxx")));

    ValidationResult actual = BeanValidator.validate(listBean);

    assertEquals(ValidationResult.ok(), actual);
  }

  @Test
  void should_return_one_error_when_validate_given_default_group_and_invalid_bean() {
    ListBean listBean = new ListBean();
    listBean.setInnerBeans(Lists.of(new InnerBean("xxx")));

    ValidationResult actual = BeanValidator.validate(listBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("name", getMessage(NotBlank.class))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_inner_not_empty_error_when_validate_given_default_group_and_invalid_bean() {
    ListBean listBean = new ListBean();

    ValidationResult actual = BeanValidator.validate(listBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from("innerBeans", getMessage(NotEmpty.class))
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_inner_name_not_empty_error_when_validate_given_default_group_and_invalid_bean() {
    ListBean listBean = new ListBean();
    listBean.setInnerBeans(Lists.of(new InnerBean("")));

    ValidationResult actual = BeanValidator.validate(listBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("name", getMessage(NotBlank.class)),
            FieldResult.from("innerBeans[0].innerName", getMessage(NotBlank.class))
        )
    );
    assertEquals(expected, actual);
  }

  @Test
  void should_return_two_inner_name_not_empty_error_when_validate_given_default_group_and_invalid_bean() {
    ListBean listBean = new ListBean();
    listBean.setInnerBeans(Lists.of(new InnerBean(""), new InnerBean("")));

    ValidationResult actual = BeanValidator.validate(listBean);

    ValidationResult expected = ValidationResult.error(
        UnionResult.from(
            FieldResult.from("name", getMessage(NotBlank.class)),
            FieldResult.from("innerBeans[0].innerName", getMessage(NotBlank.class)),
            FieldResult.from("innerBeans[1].innerName", getMessage(NotBlank.class))
        )
    );
    assertEquals(expected, actual);
  }
}
