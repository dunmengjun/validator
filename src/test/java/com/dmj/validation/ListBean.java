package com.dmj.validation;

import com.dmj.validation.constraint.NotEmpty;
import java.util.List;

public class ListBean {

  @NotEmpty
  @NotEmpty(groups = OtherGroup.class)
  @Valid
  private List<InnerBean> innerBeans;

  interface OtherGroup {

  }
}
