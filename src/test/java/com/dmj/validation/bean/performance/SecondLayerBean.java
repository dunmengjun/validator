package com.dmj.validation.bean.performance;

import com.dmj.validation.Valid;
import com.dmj.validation.constraint.Digits;
import com.dmj.validation.constraint.Email;
import com.dmj.validation.constraint.Max;
import com.dmj.validation.constraint.Min;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.Size;
import lombok.Data;
import java.util.List;

@Data
public class SecondLayerBean {

  @NotBlank
  @Digits(integer = Integer.MAX_VALUE, fraction = 8)
  private String id;
  @Max(1000L)
  private Double max;
  @Min(0L)
  private Double min;

  @Size(min = 10, max = 20)
  private String text;

  @Email
  private String email;

  @Valid
  private List<ThirdLayerBean> thirdLayerBeans;
}
