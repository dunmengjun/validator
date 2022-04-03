package com.dmj.validation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ConfigurationValue {

  public static ConfigurationValue DEFAULT_VALUE = defaultValue();

  private ValidationType validationType;

  public static ConfigurationValue from(Configuration configuration) {
    return new ConfigurationValue(configuration.type());
  }

  public static ConfigurationValue defaultValue() {
    return new ConfigurationValue(ValidationType.AND);
  }
}
