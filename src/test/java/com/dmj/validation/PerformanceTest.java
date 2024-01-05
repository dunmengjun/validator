package com.dmj.validation;

import com.dmj.validation.bean.performance.PBean;
import com.dmj.validation.bean.performance.PerformanceBeanFactory;
import org.junit.jupiter.api.Test;

public class PerformanceTest {

  @Test
  void should_less_than_1_second_when_validate_given_1000000_dto_with_less_10_validators() {
    PBean pBean = PerformanceBeanFactory.create(1, 1, 100000);

    ValidationResult actual = BeanValidator.validate(pBean);

    System.out.println(actual);
  }
}
