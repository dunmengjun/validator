package com.dmj.validation;

import com.dmj.validation.constraint.Constraint;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.exception.ReflectionException;
import com.dmj.validation.validator.ConstraintValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@AllArgsConstructor
public class ValidationBeanLayout {

  private static final Map<Class<?>, ValidationBeanLayout> validationBeanLayoutMap = new HashMap<>();

  private Map<Class<?>, ValidationBean> validationBeanMap;

  @AllArgsConstructor
  @Getter
  private static class ValidationField {

    private String name;
    private Object value;
    private String message;
    private List<Class<? extends ConstraintValidator<?>>> validatedList;

    public static ValidationField fromConstraint(String name, Object obj, Constraint constraint) {
      return new ValidationField(name, obj, constraint.message(),
          Arrays.asList(constraint.validatedBy()));
    }

    public static Optional<ValidationField> fromAnnotation(String name, Object obj,
        Annotation annotation) {
      Constraint constraint = annotation.annotationType().getDeclaredAnnotation(Constraint.class);
      if (constraint == null) {
        return Optional.empty();
      }
      String message = invoke(annotation, "message", constraint.message());
      return Optional.of(
          new ValidationField(name, obj, message, Arrays.asList(constraint.validatedBy())));
    }
  }

  @Getter
  @AllArgsConstructor
  private static class UnionValidation {

    private final UnionValue unionValue;
    private Map<String, ValidationField> fieldMap;

    public static UnionValidation from(UnionValue value) {
      return new UnionValidation(value, new HashMap<>());
    }

    public void addValidationField(ValidationField field) {
      fieldMap.put(field.getName(), field);
    }
  }

  @AllArgsConstructor
  static class ValidationBean {

    private ConfigurationValue configuration;
    private Map<Integer, UnionValidation> unionValidationMap;

    public static ValidationBean empty() {
      return new ValidationBean(ConfigurationValue.defaultValue(), new HashMap<>());
    }

    public void setUnionValidation(List<Class<?>> unions, UnionValidation validation) {
      unions.stream().map(Object::hashCode)
          .forEach(union -> unionValidationMap.putIfAbsent(union, validation));
    }

    public UnionValidation getUnionValidation(Class<?> union) {
      return unionValidationMap.get(union.hashCode());
    }

    public void setConfiguration(ConfigurationValue value) {
      if (ConfigurationValue.DEFAULT_VALUE.equals(configuration)
          && !ConfigurationValue.DEFAULT_VALUE.equals(value)) {
        configuration = value;
      }
    }
  }

  public ValidationBean get(Class<?> group) {
    return validationBeanMap.get(group);
  }

  public static ValidationBean get(Object bean, Class<?> group) {
    ValidationBeanLayout validationBeanLayout = validationBeanLayoutMap.computeIfAbsent(
        bean.getClass(), beanClass -> createLayout(bean, beanClass));
    return validationBeanLayout.get(group);
  }

  private static ValidationBeanLayout createLayout(Object bean, Class<?> beanClass) {
    Map<Class<?>, ValidationBean> groupMap = new HashMap<>();
    while (!beanClass.equals(Object.class)) {
      Annotation[] classAnnotations = beanClass.getDeclaredAnnotations();
      String simpleName = bean.getClass().getSimpleName();
      for (Annotation classAnnotation : classAnnotations) {
        List<Class<?>> groups = getGroups(classAnnotation);
        if (classAnnotation instanceof Configuration) {
          ConfigurationValue value = ConfigurationValue.from((Configuration) classAnnotation);
          setConfigurationValue(groupMap, groups, value);
        } else if (classAnnotation instanceof Union) {
          Union union = (Union) classAnnotation;
          List<Class<?>> unions = Arrays.asList(union.unions());
          UnionValue value = UnionValue.from(union);
          setUnionValue(groupMap, groups, unions, value);
        } else {
          initValidationField(bean, groupMap, simpleName, classAnnotation, groups);
        }
      }
      Field[] declaredFields = beanClass.getDeclaredFields();
      for (Field field : declaredFields) {
        Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
        String name = field.getName();
        Object value = getValue(field, bean);
        for (Annotation fieldAnnotation : fieldAnnotations) {
          List<Class<?>> groups = getGroups(fieldAnnotation);
          initValidationField(value, groupMap, name, fieldAnnotation, groups);
        }
      }
      beanClass = beanClass.getSuperclass();
    }
    ValidationBeanLayout value = new ValidationBeanLayout(groupMap);
    validationBeanLayoutMap.put(bean.getClass(), value);
    return value;
  }

  private static void initValidationField(Object bean, Map<Class<?>, ValidationBean> groupMap,
      String simpleName, Annotation classAnnotation, List<Class<?>> groups) {
    if (classAnnotation instanceof Constraint) {
      Constraint constraint = (Constraint) classAnnotation;
      List<Class<?>> unions = Arrays.asList(constraint.unions());
      ValidationField field = ValidationField.fromConstraint(simpleName, bean, constraint);
      setValidationField(groupMap, groups, unions, field);
    } else {
      Class<?>[] unions = invoke(classAnnotation, "unions", new Class<?>[]{});
      ValidationField.fromAnnotation(simpleName, bean, classAnnotation)
          .ifPresent(field -> setValidationField(groupMap, groups, Arrays.asList(unions), field));
    }
  }

  private static void setUnionValue(Map<Class<?>, ValidationBean> groupMap, List<Class<?>> groups,
      List<Class<?>> unions, UnionValue value) {
    groups.forEach(group -> {
      ValidationBean validationBean = groupMap.computeIfAbsent(group, g -> ValidationBean.empty());
      validationBean.setUnionValidation(unions, UnionValidation.from(value));
    });
  }

  private static void setValidationField(Map<Class<?>, ValidationBean> groupMap,
      List<Class<?>> groups, List<Class<?>> unions, ValidationField field) {
    groups.forEach(group -> {
      ValidationBean validationBean = groupMap.computeIfAbsent(group, g -> ValidationBean.empty());
      unions.forEach(union -> {
        UnionValidation unionValidation = validationBean.getUnionValidation(union);
        if (unionValidation != null) {
          unionValidation.addValidationField(field);
        }
      });
    });
  }

  private static void setConfigurationValue(Map<Class<?>, ValidationBean> groupMap,
      List<Class<?>> groups, ConfigurationValue value) {
    groups.forEach(group -> {
      ValidationBean validationBean = groupMap.computeIfAbsent(group, g -> ValidationBean.empty());
      validationBean.setConfiguration(value);
    });
  }


  private static List<Class<?>> getGroups(Annotation annotation) {
    return Arrays.asList(invoke(annotation, "groups", new Class<?>[]{Default.class}));
  }

  @SuppressWarnings("unchecked")
  private static <T> T invoke(Object object, String name, T defaultValue) {
    try {
      Method groups = object.getClass().getDeclaredMethod(name);
      return (T) groups.invoke(object);
    } catch (NoSuchMethodException e) {
      return defaultValue;
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  }

  private static Object getValue(Field field, Object bean) {
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
}
