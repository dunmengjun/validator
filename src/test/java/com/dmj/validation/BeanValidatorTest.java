package com.dmj.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BeanValidatorTest {

  @Test
  void should_return_result_when_validate_bean_given_invalid_fields() {
    TestBean bean = new TestBean();

    ValidationResult actual = BeanValidator.validate(bean);

    System.out.println(actual);

    assertEquals(5, actual.getResults().size());
  }

  @Test
  void should_return_empty_result_when_validate_bean_given_valid_fields() {
    TestBean bean = new TestBean();
    bean.setAge(17);
    bean.setId(1L);
    bean.setName("alice");
    bean.setType("type");
    InnerBean innerBean = new InnerBean();
    bean.setInnerBean(innerBean);

    ValidationResult actual = BeanValidator.validate(bean);

    assertEquals(0, actual.getResults().size());
  }
}
