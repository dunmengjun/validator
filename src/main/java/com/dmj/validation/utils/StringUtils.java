package com.dmj.validation.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static boolean isBlank(String value) {
    return value == null || value.trim().equals("");
  }

  public static boolean isNotBlank(String value) {
    return !isBlank(value);
  }
}
