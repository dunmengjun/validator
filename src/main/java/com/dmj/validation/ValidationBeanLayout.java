package com.dmj.validation;

import static com.dmj.validation.ConfigurationValue.DEFAULT_VALUE;
import static com.dmj.validation.utils.ReflectionUtils.invokeMethod;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.dmj.validation.constraint.Constraint;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.exception.UnionAnnotationException;
import com.dmj.validation.utils.Asserts;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.Maps;
import com.dmj.validation.utils.ReflectionUtils;
import com.dmj.validation.validator.ConstraintValidator;
import com.dmj.validation.validator.UnionValidator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
public class ValidationBeanLayout {

  private static final Map<Class<?>, ValidationBeanLayout> validationBeanLayoutMap = new HashMap<>();

  private Map<Class<?>, ValidationBean> validationBeanMap;

  @AllArgsConstructor
  @Getter
  @EqualsAndHashCode
  public static class ValidationField {

    private Annotation annotation;
    private boolean isNullValid;
    private Field field;
    private String message;
    private List<Class<? extends ConstraintValidator<?, ?>>> validatedBy;

    public static ValidationField fromConstraint(Field field, Constraint constraint) {
      return new ValidationField(constraint, constraint.isNullValid(),
          field, constraint.message(), asList(constraint.validatedBy()));
    }

    public static Optional<ValidationField> fromAnnotation(Field field,
        Annotation annotation) {
      Constraint constraint = annotation.annotationType().getDeclaredAnnotation(Constraint.class);
      if (constraint == null) {
        return Optional.empty();
      }
      String message = invokeMethod(annotation, "message", constraint.message());
      return Optional.of(new ValidationField(annotation, constraint.isNullValid(),
          field, message, asList(constraint.validatedBy())));
    }
  }

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class ValidationUnion {

    private UnionValue unionValue;
    private List<ValidationField> fields;

    public static ValidationUnion from(UnionValue value) {
      return new ValidationUnion(value, Lists.of());
    }

    public static ValidationUnion from(ValidationField value) {
      return new ValidationUnion(UnionValue.empty(), Lists.of(value));
    }

    public ValidationUnion add(ValidationUnion validation) {
      if (this.equals(validation)) {
        return this;
      }
      if (UnionValue.empty().equals(unionValue)) {
        this.unionValue = validation.unionValue;
      }
      this.fields.addAll(validation.fields);
      return this;
    }
  }

  @AllArgsConstructor
  @Getter
  public static class ValidationBean {

    private ConfigurationValue configuration;
    private Map<Field, ValidationBean> needExtendBeanMap;
    private Map<Integer, ValidationUnion> validationUnionMap;

    public static ValidationBean empty() {
      return new ValidationBean(ConfigurationValue.defaultValue(), Maps.of(), new HashMap<>());
    }

    public void setUnionValidation(List<Integer> unions, ValidationUnion validation) {
      unions.forEach(union -> validationUnionMap.merge(union, validation, ValidationUnion::add));
    }

    public void setConfiguration(ConfigurationValue value) {
      if (DEFAULT_VALUE.equals(configuration) && !DEFAULT_VALUE.equals(value)) {
        configuration = value;
      }
    }
  }

  public Optional<ValidationBean> get(Class<?> group) {
    return Optional.ofNullable(validationBeanMap.get(group));
  }

  public static Optional<ValidationBean> get(Object bean, Class<?> group) {
    return get(bean.getClass(), group);
  }

  private static Optional<ValidationBean> get(Class<?> beanClass,
      Class<?> group) {
    ValidationBeanLayout validationBeanLayout = validationBeanLayoutMap
        .computeIfAbsent(beanClass, ValidationBeanLayout::createLayout);
    return validationBeanLayout.get(group);
  }

  private static List<Annotation> getAnnotationList(Annotation annotation) {
    Method[] declaredMethods = annotation.annotationType().getDeclaredMethods();
    if (declaredMethods.length != 1) {
      return Lists.of(annotation);
    }
    Repeatable repeatable = annotation.annotationType().getDeclaredAnnotation(Repeatable.class);
    Constraint declaredAnnotation = annotation.annotationType()
        .getDeclaredAnnotation(Constraint.class);
    if (repeatable != null || declaredAnnotation != null) {
      return Lists.of(annotation);
    }
    boolean flag = ReflectionUtils.hasMethod(annotation.annotationType(), "value",
        Annotation[].class);
    if (!flag) {
      return Lists.of(annotation);
    }
    Annotation[] values = invokeMethod(annotation, "value");
    return Lists.of(values);
  }

  private static ValidationBeanLayout createLayout(Class<?> beanClass) {
    Map<Class<?>, ValidationBean> groupMap = new HashMap<>();
    Class<?> originBeanClass = beanClass;
    while (!beanClass.equals(Object.class)) {
      Annotation[] classAnnotations = beanClass.getDeclaredAnnotations();
      for (Annotation classAnnotation : classAnnotations) {
        List<Annotation> annotationList = getAnnotationList(classAnnotation);
        for (Annotation annotation : annotationList) {
          List<Class<?>> groups = getGroups(annotation, Lists.of(Default.class));
          if (annotation instanceof Configuration) {
            ConfigurationValue value = ConfigurationValue.from((Configuration) annotation);
            setConfigurationValue(groupMap, groups, value);
          } else if (annotation instanceof Union) {
            Union union = (Union) annotation;
            List<Integer> unions = mapOrDefault(union.unions(), Objects::hashCode, Lists.of());
            Asserts.assertNotEmpty(unions, UnionAnnotationException::new);
            Asserts.assertNotNull(union.message(), UnionAnnotationException::new);
            ValidationUnion from = ValidationUnion.from(UnionValue.from(union));
            setUnionValidation(groupMap, groups, unions, from);
          } else {
            initValidationField(originBeanClass.hashCode(), null, groupMap, annotation, groups);
          }
        }
      }
      Field[] declaredFields = beanClass.getDeclaredFields();
      for (Field field : declaredFields) {
        Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
        for (Annotation fieldAnnotation : fieldAnnotations) {
          List<Annotation> annotationList = getAnnotationList(fieldAnnotation);
          for (Annotation annotation : annotationList) {
            List<Class<?>> groups = getGroups(annotation, Lists.of(Default.class));
            if (annotation instanceof Valid) {
              Class<?> type = getFieldType(field);
              groups.forEach(
                  g -> ValidationBeanLayout.get(type, g).ifPresent(validationBean -> {
                    ValidationBean groupValidationBean = groupMap.computeIfAbsent(g,
                        gg -> ValidationBean.empty());
                    groupValidationBean.getNeedExtendBeanMap().put(field, validationBean);
                  }));
            } else if (annotation instanceof Union) {
              Union union = (Union) annotation;
              Class<? extends UnionValidator>[] classes = union.validatedBy();
              if (classes.length == 0) {
                continue;
              }
              ValidationUnion from = ValidationUnion.from(UnionValue.from(union));
              setUnionValidation(groupMap, groups, Lists.of(field.hashCode()), from);
            } else {
              initValidationField(field.hashCode(), field, groupMap, annotation, groups);
            }
          }
        }
      }
      beanClass = beanClass.getSuperclass();
    }
    ValidationBeanLayout value = new ValidationBeanLayout(groupMap);
    validationBeanLayoutMap.put(originBeanClass, value);
    return value;
  }

  private static Class<?> getFieldType(Field field) {
    Class<?> type = field.getType();
    if (Collection.class.isAssignableFrom(type)) {
      type = ReflectionUtils.getFirstGenericType(field);
    }
    return type;
  }

  private static void initValidationField(Integer union, Field field,
      Map<Class<?>, ValidationBean> groupMap,
      Annotation annotation, List<Class<?>> groups) {
    if (annotation instanceof Constraint) {
      Constraint constraint = (Constraint) annotation;
      List<Integer> unions = mapOrDefault(constraint.unions(), Objects::hashCode,
          Lists.of(union));
      ValidationField validationField = ValidationField.fromConstraint(field, constraint);
      ValidationUnion from = ValidationUnion.from(validationField);
      setUnionValidation(groupMap, groups, unions, from);
    } else {
      Class<?>[] unionsClasses = invokeMethod(annotation, "unions", new Class<?>[]{});
      List<Integer> unions = mapOrDefault(unionsClasses, Objects::hashCode,
          Lists.of(union));
      ValidationField.fromAnnotation(field, annotation).ifPresent(validationField -> {
        ValidationUnion from = ValidationUnion.from(validationField);
        setUnionValidation(groupMap, groups, unions, from);
      });
    }
  }

  private static <T, R> List<R> mapOrDefault(T[] array, Function<T, R> mapFunction,
      List<R> defaultValue) {
    if (array == null || array.length == 0) {
      return defaultValue;
    }
    return Arrays.stream(array).map(mapFunction).collect(toList());
  }

  private static void setUnionValidation(Map<Class<?>, ValidationBean> groupMap,
      List<Class<?>> groups, List<Integer> unions, ValidationUnion validation) {
    groups.forEach(group -> {
      ValidationBean validationBean = groupMap.computeIfAbsent(group, g -> ValidationBean.empty());
      validationBean.setUnionValidation(unions, validation);
    });
  }

  private static void setConfigurationValue(Map<Class<?>, ValidationBean> groupMap,
      List<Class<?>> groups, ConfigurationValue value) {
    groups.forEach(group -> {
      ValidationBean validationBean = groupMap.computeIfAbsent(group, g -> ValidationBean.empty());
      validationBean.setConfiguration(value);
    });
  }

  private static List<Class<?>> getGroups(Annotation annotation, List<Class<?>> defaultValue) {
    Class<?>[] groups = invokeMethod(annotation, "groups", new Class[0]);
    return groups.length == 0 ? defaultValue : asList(groups);
  }
}
