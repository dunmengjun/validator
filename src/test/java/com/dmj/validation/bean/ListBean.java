package com.dmj.validation.bean;

import com.dmj.validation.Valid;
import com.dmj.validation.bean.ListBean.FullNotEmpty;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.validator.union.FullMatch;
import java.util.List;
import lombok.Data;

@Data
@Union(unions = FullNotEmpty.class, validatedBy = FullMatch.class)
public class ListBean {

  @NotBlank(unions = FullNotEmpty.class)
  private String name;

  @NotEmpty
  @Valid
  private List<InnerBean> innerBeans;

  interface FullNotEmpty {

  }
}
