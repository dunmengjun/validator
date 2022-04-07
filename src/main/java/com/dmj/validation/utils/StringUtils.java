package com.dmj.validation.utils;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class StringUtils {

  private static final Pattern pattern = Pattern.compile("\\{(.*?)}");


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

  public static String format(String source, BiFunction<Integer, String, Object> function) {
    Matcher matcher = pattern.matcher(source);
    int index = 0;
    while (matcher.find()) {
      String key = matcher.group();
      String keyClone = key.substring(1, key.length() - 1).trim();
      try {
        Object value = function.apply(index, keyClone);
        if (value != null) {
          source = source.replace(key, value.toString());
        }
      } catch (Exception ignored) {
      }
      index++;
    }
    return source;
  }

  public static String format(String source, Object... args) {
    if (args == null) {
      return source;
    }
    return format(source, (index, key) -> args[index]);
  }
}
