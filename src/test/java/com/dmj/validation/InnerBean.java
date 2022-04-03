package com.dmj.validation;

import com.dmj.validation.TestBean.AnyNotEmpty;
import com.dmj.validation.constraint.NotBlank;

public class InnerBean {

  @NotBlank(unions = AnyNotEmpty.class)
  private String innerName;
}
