package com.dmj.validation.utils;

import java.util.Collection;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Asserts {

  public static void assertNotEmpty(Collection<?> collection,
      Supplier<? extends RuntimeException> supplier) {
    if (collection == null || collection.isEmpty()) {
      throw supplier.get();
    }
  }

  public static void assertNotNull(Object object,
      Supplier<? extends RuntimeException> supplier) {
    if (object == null) {
      throw supplier.get();
    }
  }
}
