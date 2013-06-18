package com.bytedistillers.payment.sofort.classic;

import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofortClassicTemplate {

  private static final Logger logger = LoggerFactory.getLogger(SofortClassicTemplate.class);

  private String formActionUrl = "https://www.sofort.com/payment/start";
  private String formTarget;
  private String formName = "sofortClassicForm";
  private ClassicFormData formData;

  public String generateHtmlForm() {
    StringBuilder result = new StringBuilder("<form action=\"" + formActionUrl + "\" method=\"POST\" target=\"" + formTarget + "\" name=\"" + formName + "\">\r\n");

    Method[] methods = ClassicFormData.class.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        Object value = getReturnValue(method);
        if (value != null) {
          String fieldName = method.getName().replaceFirst("get", "");
          fieldName = StringUtils.uncapitalize(fieldName);
          if (StringUtils.equals(fieldName, "class") || StringUtils.equals(fieldName, "userVariables")) {
            continue;
          }
          result.append(createInput(fieldName, value.toString()));
        }
      }
    }

    for (Entry<String, String> entry : formData.getUserVariables().entrySet()) {
      String fieldName = entry.getKey();
      String fieldValue = entry.getValue();
      result.append(createInput(fieldName, fieldValue));
    }
    result.append("</form>\r\n");

    return result.toString();
  }

  private String createInput(String name, String value) {
    return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />\r\n";
  }

  private Object getReturnValue(Method method) {
    Object result;
    try {
      result = method.invoke(formData);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      result = null;
    }
    return result;
  }

  public String getFormActionUrl() {
    return formActionUrl;
  }

  public void setFormActionUrl(String formActionUrl) {
    this.formActionUrl = formActionUrl;
  }

  public String getFormTarget() {
    return formTarget;
  }

  public void setFormTarget(String formTarget) {
    this.formTarget = formTarget;
  }

  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public ClassicFormData getFormData() {
    return formData;
  }

  public void setFormData(ClassicFormData formData) {
    this.formData = formData;
  }

}
