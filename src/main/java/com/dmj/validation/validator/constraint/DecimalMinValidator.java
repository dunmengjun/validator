package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.DecimalMin;
import com.dmj.validation.validator.ConstraintValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecimalMinValidator implements ConstraintValidator<Object, DecimalMin> {

  @Override
  public boolean valid(Object value, DecimalMin annotation) {
    try {
      double min = Double.parseDouble(String.valueOf(annotation.value()));
      double v = Double.parseDouble(String.valueOf(value));
      return v >= min;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }
}
