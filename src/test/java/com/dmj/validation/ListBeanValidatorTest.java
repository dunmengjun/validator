package com.dmj.validation;

import com.dmj.validation.bean.ListBean;
import org.junit.jupiter.api.Test;

public class ListBeanValidatorTest {

  @Test
  void test() {
    ListBean listBean = new ListBean();
    ValidationResult validate = BeanValidator.validate(listBean);
    System.out.println(validate);
  }
}
