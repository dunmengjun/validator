package com.dmj.validation.bean.performance;

import java.util.ArrayList;
import java.util.List;

public class PerformanceBeanFactory {

  public static PBean create(int firstLayerSize, int secondLayerSize, int thirdLayerSize) {
    PBean pBean = new PBean();
    pBean.setId("123456");
    pBean.setMax(500d);
    pBean.setMin(100d);
    pBean.setText("xxxxxxxxxxxx");
    pBean.setEmail("test@dd.com");
    List<FirstLayerBean> firstLayerBeans = new ArrayList<>(firstLayerSize);
    for (int i = 0; i < firstLayerSize; i++) {
      firstLayerBeans.add(randomFirstLayerBean(secondLayerSize, thirdLayerSize));
    }
    pBean.setFirstLayerBeans(firstLayerBeans);
    return pBean;
  }

  private static FirstLayerBean randomFirstLayerBean(int secondLayerSize, int thirdLayerSize) {
    FirstLayerBean firstLayerBean = new FirstLayerBean();
    firstLayerBean.setId("123456");
    firstLayerBean.setMax(500d);
    firstLayerBean.setMin(100d);
    firstLayerBean.setText("xxxxxxxxxxxx");
    firstLayerBean.setEmail("test@dd.com");
    List<SecondLayerBean> secondLayerBeans = new ArrayList<>(secondLayerSize);
    for (int i = 0; i < secondLayerSize; i++) {
      secondLayerBeans.add(randomSecondLayerBean(thirdLayerSize));
    }
    firstLayerBean.setSecondLayerBeans(secondLayerBeans);
    return firstLayerBean;
  }

  private static SecondLayerBean randomSecondLayerBean(int thirdLayerSize) {
    SecondLayerBean secondLayerBean = new SecondLayerBean();
    secondLayerBean.setId("123456");
    secondLayerBean.setMax(500d);
    secondLayerBean.setMin(100d);
    secondLayerBean.setText("xxxxxxxxxxxx");
    secondLayerBean.setEmail("test@dd.com");
    List<ThirdLayerBean> thirdLayerBeans = new ArrayList<>(thirdLayerSize);
    for (int i = 0; i < thirdLayerSize; i++) {
      thirdLayerBeans.add(randomThirdLayerBean());
    }
    secondLayerBean.setThirdLayerBeans(thirdLayerBeans);
    return secondLayerBean;
  }

  private static ThirdLayerBean randomThirdLayerBean() {
    ThirdLayerBean thirdLayerBean = new ThirdLayerBean();
    thirdLayerBean.setId("123456");
    thirdLayerBean.setMax(500d);
    thirdLayerBean.setMin(100d);
    thirdLayerBean.setText("xxxxxxxxxxxx");
    thirdLayerBean.setEmail("test@dd.com");
    return thirdLayerBean;
  }
}
