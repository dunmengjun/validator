package com.dmj.validation.utils;

import com.dmj.validation.exception.ReflectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

  public static <T> T invokeMethod(Object object, String name, T defaultValue) {
    try {
      return invokeMethod(object, name);
    } catch (ReflectionException e) {
      if (e.getCause() instanceof NoSuchMethodException) {
        return defaultValue;
      }
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T invokeMethod(Object object, String name) {
    try {
      Method groups = object.getClass().getDeclaredMethod(name);
      groups.setAccessible(true);
      return (T) groups.invoke(object);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  public static Object getValue(Field field, Object bean) {
    if (bean == null) {
      return null;
    }
    try {
      if (field.isAccessible()) {
        return field.get(bean);
      } else {
        field.setAccessible(true);
        Object o = field.get(bean);
        field.setAccessible(false);
        return o;
      }
    } catch (IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  public static Class<?> getFirstGenericType(Field field) {
    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
    return (Class<?>) genericType.getActualTypeArguments()[0];
  }

  public static boolean hasMethod(Class<?> targetClass, String name, Class<?> result,
      Class<?>... args) {
    Method declaredMethod;
    try {
      declaredMethod = targetClass.getDeclaredMethod(name, args);
    } catch (NoSuchMethodException e) {
      return false;
    }
    Class<?> returnType = declaredMethod.getReturnType();
    return returnType.equals(result) || result.isAssignableFrom(returnType);
  }
}
