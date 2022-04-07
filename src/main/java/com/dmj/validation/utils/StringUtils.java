package com.dmj.validation.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class StringUtils {

  public static boolean isBlank(CharSequence value) {
    int strLen;
    if (value == null || (strLen = value.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((!Character.isWhitespace(value.charAt(i)))) {
        return false;
      }
    }
    return true;
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

  public static String join(String join, String... args) {
    return Stream.of(args).filter(StringUtils::isNotBlank).collect(Collectors.joining(join));
  }
}
