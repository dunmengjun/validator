package com.dmj.validation.utils;

import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Lists {

  public static <T> List<T> of(T... objects) {
    if (objects == null) {
      return new ArrayList<>(0);
    }
    return Arrays.stream(objects).collect(Collectors.toList());
  }
}
