package com.bytedistillers.payment.sofort.classic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SofortClassicTemplateTest {

  private SofortClassicTemplate template;

  @Before
  public void setup() {
    ClassicFormData staticFormData = new ClassicFormData();
    staticFormData.setCurrency_id("EUR");
    staticFormData.setReason_1("My reason");
    staticFormData.setUser_id("user_id");
    staticFormData.setProject_id("project_id");

    Map<String, String> userdefined = new HashMap<String, String>();
    userdefined.put("static", "statisch");
    userdefined.put("mixed", "statisch");
    staticFormData.setUserVariables(userdefined);

    template = new SofortClassicTemplate(staticFormData);
  }

  @Test
  public void generateHtmlForm() {
    ClassicFormData dynamicFormData = new ClassicFormData();
    dynamicFormData.setAmount(BigDecimal.valueOf(123.45));
    dynamicFormData.setUser_id("other_user_id");

    Map<String, String> userdefined = new HashMap<String, String>();
    userdefined.put("dynamic", "dynamisch");
    userdefined.put("mixed", "gemischt");
    dynamicFormData.setUserVariables(userdefined);

    String result = template.generateHtmlForm(dynamicFormData);
    System.out.println(result);
  }
}
