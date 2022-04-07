package com.dmj.validation.bean;

import com.dmj.validation.Configuration;
import com.dmj.validation.Valid;
import com.dmj.validation.bean.UnionBean.AllNotEmpty;
import com.dmj.validation.bean.UnionBean.AnyNotEmpty;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.constraint.Size;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.validator.union.AllMatch;
import com.dmj.validation.validator.union.AnyMatch;
import com.dmj.validation.validator.union.FullMatch;
import lombok.Data;

@Data
@Configuration(validatedBy = FullMatch.class)
@Union(
    unions = AnyNotEmpty.class,
    validatedBy = AnyMatch.class,
    message = "Can't all be empty"
)
@Union(unions = AllNotEmpty.class, validatedBy = AllMatch.class)
public class UnionBean {

  @NotNull(unions = {
      AnyNotEmpty.class,
      AllNotEmpty.class
  })
  private Integer age;

  @NotBlank(unions = {
      AnyNotEmpty.class,
      AllNotEmpty.class
  })
  private String type;


  @NotBlank(groups = AnyMatch.class)
  @Size(min = 2, max = 4, groups = AnyMatch.class)
  @Pattern(regexp = "\\d+", groups = AnyMatch.class)
  @Union(validatedBy = AnyMatch.class, groups = AnyMatch.class)
  private String thirdName;

  @Valid
  private InnerBean innerBean;

  interface AnyNotEmpty {

  }

  interface AllNotEmpty {

  }
}
