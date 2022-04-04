package com.dmj.validation;

import com.dmj.validation.validator.UnionValidator;
import com.dmj.validation.validator.union.AnyMatch;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ConfigurationValue {

  public static ConfigurationValue DEFAULT_VALUE = defaultValue();

  private Class<? extends UnionValidator> validatedBy;

  public static ConfigurationValue from(Configuration configuration) {
    return new ConfigurationValue(configuration.validatedBy());
  }

  public static ConfigurationValue defaultValue() {
    return new ConfigurationValue(AnyMatch.class);
  }
}
