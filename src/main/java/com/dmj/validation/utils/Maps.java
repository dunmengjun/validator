package com.dmj.validation.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Maps {

  public static <K, V> Map<K, V> of() {
    return new HashMap<>(0);
  }

  public static <K, V> Map<K, V> of(K key, V value) {
    Map<K, V> map = new HashMap<>(1);
    map.put(key, value);
    return map;
  }

  public static <K, V> Map<K, V> merge(Map<K, V> map1, Map<K, V> map2,
      BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    Map<K, V> result = new HashMap<>();
    for (Map<K, V> map : Arrays.asList(map1, map2)) {
      for (Entry<K, V> kvEntry : map.entrySet()) {
        result.merge(kvEntry.getKey(), kvEntry.getValue(), remappingFunction);
      }
    }
    return result;
  }
}
