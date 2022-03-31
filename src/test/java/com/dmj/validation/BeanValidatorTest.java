package com.dmj.validation;

import org.junit.jupiter.api.Test;

public class BeanValidatorTest {

  @Test
  void test() {
    TestBean bean = new TestBean();
    ValidationResult validate = BeanValidator.validate(bean);
    System.out.println(validate);
  }
}
