package com.dmj.validation.bean;

import com.dmj.validation.Configuration;
import com.dmj.validation.constraint.Max;
import com.dmj.validation.constraint.Min;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.validator.union.AnyMatch;
import com.dmj.validation.validator.union.FullMatch;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Configuration(groups = FullMatch.class, validatedBy = FullMatch.class)
@Configuration(groups = AnyMatch.class, validatedBy = AnyMatch.class)
public class ConfigurationBean {

  @NotNull(groups = FullMatch.class)
  @NotBlank(groups = AnyMatch.class)
  @NotEmpty
  private String name;

  @Min(value = 10, groups = FullMatch.class)
  @Max(value = 20, groups = AnyMatch.class)
  @Min(15)
  private Integer number;

  public ConfigurationBean(Integer number) {
    this.number = number;
  }

  public ConfigurationBean(String name, Integer number) {
    this.name = name;
    this.number = number;
  }
}
