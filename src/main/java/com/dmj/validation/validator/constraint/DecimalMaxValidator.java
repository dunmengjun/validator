package com.dmj.validation.validator.constraint;

import com.dmj.validation.constraint.DecimalMax;
import com.dmj.validation.validator.ConstraintValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DecimalMaxValidator implements ConstraintValidator<Object, DecimalMax> {

  @Override
  public boolean valid(Object value, DecimalMax annotation) {
    try {
      double max = Double.parseDouble(String.valueOf(annotation.value()));
      double v = Double.parseDouble(String.valueOf(value));
      return v <= max;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
  }
}
