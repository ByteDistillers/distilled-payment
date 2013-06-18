package com.bytedistillers.payment.sofort.classic;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytedistillers.payment.common.form.FormDataMergeUtil;
import com.bytedistillers.payment.common.form.FormGeneratorUtil;

public class SofortClassicTemplate {

  private static final Logger logger = LoggerFactory.getLogger(SofortClassicTemplate.class);

  private String formActionUrl = "https://www.sofort.com/payment/start";
  private String formTarget;
  private String formName = "sofortClassicForm";
  private ClassicFormData staticFormData;

  private FormGeneratorUtil formGenUtil = new FormGeneratorUtil();
  private FormDataMergeUtil<ClassicFormData> formDataCopyUtil = new FormDataMergeUtil<ClassicFormData>();

  public SofortClassicTemplate() {
  }
  
  public SofortClassicTemplate(ClassicFormData staticFormData) {
    this.staticFormData = staticFormData;
  }
  
  public String generateHtmlForm(ClassicFormData dynamicFormData) {
    ClassicFormData mergedFormData = mergeFormData(dynamicFormData);

    StringBuilder result = new StringBuilder();
    String toAppend = formGenUtil.generateFormStartTag(formActionUrl, formTarget, formName);
    result.append(toAppend);

    toAppend = formGenUtil.generateFieldValues(ClassicFormData.class, mergedFormData);
    result.append(toAppend);

    for (Entry<String, String> entry : mergedFormData.getUserVariables().entrySet()) {
      String fieldName = entry.getKey();
      String fieldValue = entry.getValue();
      result.append(createInput(fieldName, fieldValue));
    }
    result.append("</form>\r\n");

    return result.toString();
  }

  private ClassicFormData mergeFormData(ClassicFormData dynamicFormData) {
    ClassicFormData mergedFormData = formDataCopyUtil.merge(dynamicFormData, staticFormData, ClassicFormData.class);
    return mergedFormData;
  }

  private String createInput(String name, String value) {
    return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />\r\n";
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

  public ClassicFormData getStaticFormData() {
    return staticFormData;
  }

}
