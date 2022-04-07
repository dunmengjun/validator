package com.dmj.validation.bean;

import com.dmj.validation.bean.ListBean.FullNotEmpty;
import com.dmj.validation.bean.UnionBean.AllNotEmpty;
import com.dmj.validation.bean.UnionBean.AnyNotEmpty;
import com.dmj.validation.constraint.NotBlank;

public class InnerBean {

  @NotBlank(unions = {
      AnyNotEmpty.class,
      AllNotEmpty.class,
      FullNotEmpty.class
  })
  private final String innerName;

  public InnerBean(String innerName) {
    this.innerName = innerName;
  }
}
