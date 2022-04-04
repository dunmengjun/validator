package com.dmj.validation.utils;

import java.util.Set;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Lists {

  @SafeVarargs
  public static <T> List<T> of(T... objects) {
    if (objects == null) {
      return new ArrayList<>(0);
    }
    return Arrays.stream(objects).collect(Collectors.toList());
  }

  public static <T> List<T> of(Set<T> set) {
    if (set == null) {
      return new ArrayList<>(0);
    }
    return new ArrayList<>(set);
  }

  public static <T> boolean isEmpty(List<T> list) {
    return list == null || list.isEmpty();
  }
}
