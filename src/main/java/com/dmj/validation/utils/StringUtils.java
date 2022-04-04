package com.dmj.validation.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static boolean isBlank(CharSequence value) {
    return value == null || value.equals("");
  }

  public static boolean isNotBlank(CharSequence value) {
    return !isBlank(value);
  }

  public static String toLowerCaseFirstOne(String s) {
    if (Character.isLowerCase(s.charAt(0))) {
      return s;
    } else {
      return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
  }
}
