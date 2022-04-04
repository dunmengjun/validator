package com.dmj.validation;

import static com.dmj.validation.ConfigurationValue.DEFAULT_VALUE;
import static com.dmj.validation.utils.ReflectionUtils.getValue;
import static com.dmj.validation.utils.ReflectionUtils.invokeMethod;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import com.dmj.validation.BeanValidator.FieldValue;
import com.dmj.validation.constraint.Constraint;
import com.dmj.validation.constraint.Default;
import com.dmj.validation.constraint.Union;
import com.dmj.validation.exception.UnionAnnotationException;
import com.dmj.validation.utils.Asserts;
import com.dmj.validation.utils.Lists;
import com.dmj.validation.utils.Maps;
import com.dmj.validation.validator.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
public class ValidationBeanLayout {

  private static final Map<Class<?>, ValidationBeanLayout> validationBeanLayoutMap = new ConcurrentHashMap<>();
  private static final AtomicInteger unionInteger = new AtomicInteger(0);

  private Map<Class<?>, ValidationBean> validationBeanMap;

  @AllArgsConstructor
  @EqualsAndHashCode
  static class FieldPath {

    private List<Field> fields;

    public static FieldPath empty() {
      return new FieldPath(Lists.of());
    }

    public static FieldPath from(FieldPath prefix, Field field) {
      List<Field> fields = new ArrayList<>(prefix.fields);
      fields.add(field);
      return new FieldPath(fields);
    }

    public FieldValue getFieldValue(Object bean) {
      if (Lists.isEmpty(fields)) {
        return new FieldValue("", bean);
      }
      List<String> names = new ArrayList<>();
      for (Field field : fields) {
        names.add(field.getName());
        bean = getValue(field, bean);
      }
      return new FieldValue(String.join(".", names), bean);
    }
  }

  @AllArgsConstructor
  @Getter
  static class ValidationField {

    private FieldPath fieldPath;
    private String message;
    private List<Class<? extends ConstraintValidator<?>>> validatedBy;

    public static ValidationField fromConstraint(FieldPath fieldPath, Constraint constraint) {
      return new ValidationField(fieldPath, constraint.message(), asList(constraint.validatedBy()));
    }

    public static Optional<ValidationField> fromAnnotation(FieldPath fieldPath,
        Annotation annotation) {
      Constraint constraint = annotation.annotationType().getDeclaredAnnotation(Constraint.class);
      if (constraint == null) {
        return Optional.empty();
      }
      String message = invokeMethod(annotation, "message", constraint.message());
      return Optional.of(
          new ValidationField(fieldPath, message, asList(constraint.validatedBy())));
    }
  }

  @Getter
  @AllArgsConstructor
  static class ValidationUnion {

    private UnionValue unionValue;
    private Map<FieldPath, ValidationField> fieldMap;

    public static ValidationUnion from(UnionValue value) {
      return new ValidationUnion(value, new HashMap<>());
    }


    public static ValidationUnion from(ValidationField value) {
      return new ValidationUnion(UnionValue.empty(), Maps.of(value.fieldPath, value));
    }

    public ValidationUnion add(ValidationUnion validation) {
      if (this.unionValue == null) {
        this.unionValue = validation.unionValue;
      }
      this.fieldMap.putAll(validation.fieldMap);
      return this;
    }
  }

  @AllArgsConstructor
  @Getter
  static class ValidationBean {

    private ConfigurationValue configuration;
    private Map<Integer, ValidationUnion> validationUnionMap;

    public static ValidationBean empty() {
      return new ValidationBean(ConfigurationValue.defaultValue(), new HashMap<>());
    }

    public void setUnionValidation(List<Integer> unions, ValidationUnion validation) {
      unions.forEach(union -> validationUnionMap.merge(union, validation, ValidationUnion::add));
    }

    public void setConfiguration(ConfigurationValue value) {
      if (DEFAULT_VALUE.equals(configuration) && !DEFAULT_VALUE.equals(value)) {
        configuration = value;
      }
    }

    public ValidationBean add(ValidationBean newValue) {
      this.validationUnionMap = Stream.of(this.validationUnionMap, newValue.validationUnionMap)
          .flatMap(map -> map.entrySet().stream())
          .collect(Collectors.toMap(Entry::getKey, Entry::getValue, ValidationUnion::add));
      return this;
    }
  }

  public ValidationBean get(Class<?> group) {
    return validationBeanMap.get(group);
  }

  public static ValidationBean get(Object bean, Class<?> group) {
    return get(FieldPath.empty(), bean.getClass(), group);
  }

  private static ValidationBean get(FieldPath fieldPath, Class<?> beanClass, Class<?> group) {
    ValidationBeanLayout validationBeanLayout = validationBeanLayoutMap.computeIfAbsent(beanClass,
        clz -> createLayout(fieldPath, clz, group));
    return validationBeanLayout.get(group);
  }

  private static ValidationBeanLayout createLayout(FieldPath prefix, Class<?> beanClass,
      Class<?> group) {
    Map<Class<?>, ValidationBean> groupMap = new HashMap<>();
    while (!beanClass.equals(Object.class)) {
      Annotation[] classAnnotations = beanClass.getDeclaredAnnotations();
      for (Annotation classAnnotation : classAnnotations) {
        List<Class<?>> groups = getGroups(classAnnotation, Lists.of(Default.class));
        if (classAnnotation instanceof Configuration) {
          ConfigurationValue value = ConfigurationValue.from((Configuration) classAnnotation);
          setConfigurationValue(groupMap, groups, value);
        } else if (classAnnotation instanceof Union) {
          Union union = (Union) classAnnotation;
          List<Integer> unions = mapOrDefault(union.unions(), Objects::hashCode, Lists.of());
          Asserts.assertNotEmpty(unions, UnionAnnotationException::new);
          Asserts.assertNotNull(union.message(), UnionAnnotationException::new);
          ValidationUnion from = ValidationUnion.from(UnionValue.from(union));
          setUnionValidation(groupMap, groups, unions, from);
        } else {
          initValidationField(prefix, groupMap, classAnnotation, groups);
        }
      }
      Field[] declaredFields = beanClass.getDeclaredFields();
      for (Field field : declaredFields) {
        Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
        FieldPath fieldPath = FieldPath.from(prefix, field);
        for (Annotation fieldAnnotation : fieldAnnotations) {
          if (fieldAnnotation instanceof Valid) {
            List<Class<?>> groups = getGroups(fieldAnnotation, Lists.of(group));
            groups.forEach(g -> {
              ValidationBean validationBean = ValidationBeanLayout.get(fieldPath, field.getType(),
                  group);
              groupMap.merge(group, validationBean, ValidationBean::add);
            });
          } else {
            List<Class<?>> groups = getGroups(fieldAnnotation, Lists.of(Default.class));
            initValidationField(fieldPath, groupMap, fieldAnnotation, groups);
          }
        }
      }
      beanClass = beanClass.getSuperclass();
    }
    ValidationBeanLayout value = new ValidationBeanLayout(groupMap);
    validationBeanLayoutMap.put(beanClass, value);
    return value;
  }

  private static void initValidationField(FieldPath fieldPath,
      Map<Class<?>, ValidationBean> groupMap, Annotation classAnnotation, List<Class<?>> groups) {
    if (classAnnotation instanceof Constraint) {
      Constraint constraint = (Constraint) classAnnotation;
      List<Integer> unions = mapOrDefault(constraint.unions(), Objects::hashCode,
          Lists.of(unionInteger.incrementAndGet()));
      ValidationField validationField = ValidationField.fromConstraint(fieldPath, constraint);
      ValidationUnion from = ValidationUnion.from(validationField);
      setUnionValidation(groupMap, groups, unions, from);
    } else {
      Class<?>[] unionsClasses = invokeMethod(classAnnotation, "unions", new Class<?>[]{});
      List<Integer> unions = mapOrDefault(unionsClasses, Objects::hashCode,
          Lists.of(unionInteger.incrementAndGet()));
      ValidationField.fromAnnotation(fieldPath, classAnnotation).ifPresent(validationField -> {
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
