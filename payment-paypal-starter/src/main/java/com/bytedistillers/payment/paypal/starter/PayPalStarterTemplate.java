package com.bytedistillers.payment.paypal.starter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytedistillers.payment.common.form.FormGeneratorUtil;

public class PayPalStarterTemplate {

  private static final Logger logger = LoggerFactory.getLogger(PayPalStarterTemplate.class);

  private String formAction = "https://www.paypal.com/cgi-bin/webscr";
  private String formTarget;
  private String formName = "paypalStarterForm";
  private PaypalFormData staticFormData;

  private FormGeneratorUtil formGenUtil = new FormGeneratorUtil();

  public String generateHtmlForm(PaypalFormData dynamicFormData) {
    String toAppend = formGenUtil.generateFormStartTag(formAction, formTarget, formName);
    StringBuilder result = new StringBuilder(toAppend);

    toAppend = formGenUtil.generateFieldValues(staticFormData.getClass(), staticFormData);
    result.append(toAppend);
    if (staticFormData instanceof StarterCartFormData) {
      List<CartItem> items = ((StarterCartFormData) staticFormData).getItems();
      int idx = 0;
      for (CartItem item : items) {
        toAppend = formGenUtil.generateFieldValues(item.getClass(), item, ("_" + ++idx));
        result.append(toAppend);
      }
    }

    result.append(formGenUtil.generateFormEndTag());

    return result.toString();
  }

  public void setFormData(PaypalFormData formData) {
    this.staticFormData = formData;
  }

  public PaypalFormData getFormData() {
    return staticFormData;
  }

  public String getFormAction() {
    return formAction;
  }

  public void setFormAction(String formAction) {
    this.formAction = formAction;
  }

  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public String getFormTarget() {
    return formTarget;
  }

  public void setFormTarget(String formTarget) {
    this.formTarget = formTarget;
  }
}
