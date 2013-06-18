package com.bytedistillers.payment.sofort.gateway.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * apiKey and customerId must be set before the parameters object is set on the template
 * 
 * @author martin
 * 
 */
public class SofortTransactionParameters {

  private String apiKey;
  private String customerId;
  private int projectId;
  private String successUrl;
  private String cancelUrl;
  private BigDecimal amount;
  private String currencyCode;
  private List<String> reasons;

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public int getProjectId() {
    return projectId;
  }

  public void setProjectId(int projectId) {
    this.projectId = projectId;
  }

  public String getSuccessUrl() {
    return successUrl;
  }

  public void setSuccessUrl(String successUrl) {
    this.successUrl = successUrl;
  }

  public String getCancelUrl() {
    return cancelUrl;
  }

  public void setCancelUrl(String cancelUrl) {
    this.cancelUrl = cancelUrl;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public List<String> getReasons() {
    List<String> result;
    if (reasons != null) {
      result = reasons;
    } else {
      result = Collections.EMPTY_LIST;
    }
    return result;
  }

  public void setReasons(List<String> reasons) {
    this.reasons = reasons;
  }
}
