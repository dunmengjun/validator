package com.dmj.validation;

import org.junit.jupiter.api.Test;

public class BeanValidatorTest {

  @Test
  void test() {
    TestBean bean = new TestBean();
    BeanValidator.validate(bean);
  }
}
