package com.dmj.validation.bean;

import com.dmj.validation.bean.TestBean.AnyNotEmpty;
import com.dmj.validation.constraint.NotBlank;

public class InnerBean {

  @NotBlank(unions = AnyNotEmpty.class)
  private String innerName;
}
