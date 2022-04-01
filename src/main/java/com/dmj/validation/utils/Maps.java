package com.dmj.validation.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Maps {

  public static <K, V> Map<K, V> of(K key, V value) {
    Map<K, V> map = new HashMap<>(1);
    map.put(key, value);
    return map;
  }
}
