package com.dmj.validation;

import static com.dmj.validation.config.GlobalConfig.nameTranslator;
import static com.dmj.validation.utils.ReflectionUtils.invokeMethod;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

@FunctionalInterface
public interface FieldLambda<T, R> extends Serializable {

  R apply(T t);

  default String getName() {
    SerializedLambda lambda = invokeMethod(this, "writeReplace");
    return nameTranslator.translate(lambda.getImplMethodName());
  }
}
