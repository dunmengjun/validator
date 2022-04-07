package com.dmj.validation.bean;

import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.constraint.Size;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.validator.union.AnyMatch;
import lombok.Setter;

@Setter
public class MultipleBean {

  @NotBlank
  @Size(min = 2, max = 4)
  @Pattern(regexp = "\\d+")
  private String name;

  @NotEmpty(groups = NotEmpty.class)
  @NotEmpty(groups = NotEmpty.class)
  private String fullName;

  @Size(min = 2, max = 6, groups = Size.class)
  @Size(min = 3, max = 7, groups = Size.class)
  private String secondName;

  @NotBlank(groups = AnyMatch.class)
  @Size(min = 2, max = 4, groups = AnyMatch.class)
  @Pattern(regexp = "\\d+", groups = AnyMatch.class)
  @Union(validatedBy = AnyMatch.class, groups = AnyMatch.class)
  private String thirdName;
}
