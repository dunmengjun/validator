package com.dmj.validation.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Lists {

  @SafeVarargs
  public static <T> List<T> of(T... objects) {
    if (Objects.isNull(objects)) {
      return new ArrayList<>(0);
    }
    return Arrays.stream(objects).collect(Collectors.toList());
  }

  public static <T> List<T> of(List<T> list, T object) {
    List<T> newList = Optional.ofNullable(list).map(ArrayList::new).orElse(new ArrayList<>());
    if (Objects.nonNull(object)) {
      newList.add(object);
    }
    return newList;
  }

  public static <T> List<T> of(Set<T> set) {
    if (set == null) {
      return new ArrayList<>(0);
    }
    return new ArrayList<>(set);
  }

  public static <T> boolean isEqualCollection(final Collection<T> a, final Collection<T> b) {
    if (a.size() != b.size()) {
      return false;
    } else {
      Map<T, Integer> mapA = getCardinalityMap(a);
      Map<T, Integer> mapB = getCardinalityMap(b);
      if (mapA.size() != mapB.size()) {
        return false;
      } else {
        for (T obj : mapA.keySet()) {
          if (getFreq(obj, mapA) != getFreq(obj, mapB)) {
            return false;
          }
        }
        return true;
      }
    }
  }

  public static <O> Map<O, Integer> getCardinalityMap(final Iterable<? extends O> coll) {
    final Map<O, Integer> count = new HashMap<>();
    for (final O obj : coll) {
      count.merge(obj, 1, Integer::sum);
    }
    return count;
  }

  private int getFreq(final Object obj, final Map<?, Integer> freqMap) {
    final Integer count = freqMap.get(obj);
    if (Objects.nonNull(count)) {
      return count;
    }
    return 0;
  }
}
