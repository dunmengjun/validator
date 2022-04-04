package com.dmj.validation.config;

import static com.dmj.validation.utils.StringUtils.toLowerCaseFirstOne;

public class PojoLikedStyleTranslator implements NameTranslator {

  @Override
  public String translate(String methodName) {
    return toLowerCaseFirstOne(methodName.substring(3));
  }
}
