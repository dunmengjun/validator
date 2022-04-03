package com.dmj.validation;

import com.dmj.validation.TestBean.AnyNotEmpty;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.validator.union.AnyMatch;
import lombok.Data;

@Data
@Configuration(type = ValidationType.AnyMatch)
@Union(unions = AnyNotEmpty.class, validatedBy = AnyMatch.class)
public class TestBean {

  @NotNull
  private Long id;

  @NotNull
  @NotBlank
  private String name;

  @NotNull(unions = AnyNotEmpty.class)
  private Integer age;

  @NotBlank(unions = AnyNotEmpty.class)
  private String type;

  @NotNull
  @Valid
  private InnerBean innerBean;

  interface AnyNotEmpty {

  }
}
