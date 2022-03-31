package com.dmj.validation;

import com.dmj.validation.ValidationBeanLayout.ValidationBean;
import com.dmj.validation.constraint.Default;

public class BeanValidator {

  public static ValidationResult validate(Object bean, Class<?> group) {
    ValidationBean validationBean = ValidationBeanLayout.get(bean, group);
    return null;
  }

  public static ValidationResult validate(Object bean) {
    return validate(bean, Default.class);
  }
}
