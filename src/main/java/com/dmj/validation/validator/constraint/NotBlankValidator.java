package com.dmj.validation.validator.constraint;

import com.dmj.validation.utils.StringUtils;
import com.dmj.validation.validator.ConstraintValidator;

public class NotBlankValidator implements ConstraintValidator<String> {

  @Override
  public boolean valid(String value) {
    return StringUtils.isNotBlank(value);
  }
}
