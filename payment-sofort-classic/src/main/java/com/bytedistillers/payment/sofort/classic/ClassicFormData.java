package com.bytedistillers.payment.sofort.classic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bytedistillers.payment.common.form.FormData;
import com.bytedistillers.payment.common.form.XmlIgnore;

public class ClassicFormData implements FormData {
  private BigDecimal amount;
  private String currency_id;
  private String reason_1;
  private String reason_2;
  private String user_id;
  private String project_id;

  @XmlIgnore
  private Map<String, String> userVariables = new HashMap<String, String>();

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
  }

  public String getCurrency_id() {
    return currency_id;
  }

  public void setCurrency_id(String currency_id) {
    this.currency_id = currency_id;
  }

  public String getReason_1() {
    return reason_1;
  }

  public void setReason_1(String reason_1) {
    this.reason_1 = StringUtils.left(reason_1, 27);
  }

  public String getReason_2() {
    return reason_2;
  }

  public void setReason_2(String reason_2) {
    this.reason_2 = reason_2;
  }

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getProject_id() {
    return project_id;
  }

  public void setProject_id(String project_id) {
    this.project_id = project_id;
  }

  public Map<String, String> getUserVariables() {
    return userVariables;
  }

  public void setUserVariables(Map<String, String> userVariables) {
    this.userVariables = userVariables;
  }

}
