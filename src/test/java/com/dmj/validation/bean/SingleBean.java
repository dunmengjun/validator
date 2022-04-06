package com.dmj.validation.bean;

import com.dmj.validation.constraint.AssertFalse;
import com.dmj.validation.constraint.AssertTrue;
import com.dmj.validation.constraint.DecimalMax;
import com.dmj.validation.constraint.DecimalMin;
import com.dmj.validation.constraint.Digits;
import com.dmj.validation.constraint.Email;
import com.dmj.validation.constraint.Max;
import com.dmj.validation.constraint.Min;
import com.dmj.validation.constraint.Negative;
import com.dmj.validation.constraint.NegativeOrZero;
import com.dmj.validation.constraint.NotBlank;
import com.dmj.validation.constraint.NotEmpty;
import com.dmj.validation.constraint.NotNull;
import com.dmj.validation.constraint.Null;
import com.dmj.validation.constraint.Pattern;
import com.dmj.validation.constraint.Positive;
import com.dmj.validation.constraint.PositiveOrZero;
import com.dmj.validation.constraint.Size;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SingleBean {

  @NotNull(groups = NotNull.class)
  @NotBlank(groups = NotBlank.class)
  @NotEmpty(groups = NotEmpty.class)
  @Size(min = 2, max = 4, groups = Size.class)
  @Pattern(regexp = "\\d+", groups = Pattern.class)
  @Null(groups = Null.class)
  @Email(groups = Email.class)
  @Digits(integer = 2, fraction = 3, groups = Digits.class)
  private String name;

  @AssertTrue(groups = AssertTrue.class)
  @AssertFalse(groups = AssertFalse.class)
  private boolean flag;

  @Positive(groups = Positive.class)
  @PositiveOrZero(groups = PositiveOrZero.class)
  @Negative(groups = Negative.class)
  @NegativeOrZero(groups = NegativeOrZero.class)
  @Min(value = 100, groups = Min.class)
  @Max(value = 200, groups = Max.class)
  private int ints;

  @DecimalMax(value = "10.32", groups = DecimalMax.class)
  @DecimalMin(value = "1.32", groups = DecimalMin.class)
  private double decimal;

  public SingleBean(String name) {
    this.name = name;
  }

  public SingleBean(boolean flag) {
    this.flag = flag;
  }

  public SingleBean(int ints) {
    this.ints = ints;
  }

  public SingleBean(double decimal) {
    this.decimal = decimal;
  }
}
