package com.bytedistillers.payment.sofort.gateway.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Multipay {
  private int projectId;
  private String languageCode;
  private String interfaceVersion;
  private Integer timeout;
  private String emailCustomer;
  private String phoneCustomer;

  private List<String> userVariables;

  private Sender sender;

  private BigDecimal amount;
  private String currencyCode;

  private List<String> reasons;

  private String successUrl;
  private String abortUrl;
  private String timeoutUrl;
  private List<String> notificationUrls;
  private List<String> notificationEmails;

  private boolean customerProtection;

  public Multipay(int projectId, BigDecimal amount, String currencyCode, String... reasons) {
    this.projectId = projectId;
    this.amount = amount;
    this.currencyCode = currencyCode;

    setReasons(reasons);
  }

  private void setReasons(String[] reasons) {
    this.reasons = new ArrayList<String>();
    int maxIdx = reasons.length > 2 ? 2 : reasons.length;
    for (int idx = 0; idx < maxIdx; ++idx) {
      this.reasons.add(reasons[idx]);
    }
  }

  public int getProjectId() {
    return projectId;
  }

  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public String getInterfaceVersion() {
    return interfaceVersion;
  }

  public void setInterfaceVersion(String interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public String getEmailCustomer() {
    return emailCustomer;
  }

  public void setEmailCustomer(String emailCustomer) {
    this.emailCustomer = emailCustomer;
  }

  public String getPhoneCustomer() {
    return phoneCustomer;
  }

  public void setPhoneCustomer(String phoneCustomer) {
    this.phoneCustomer = phoneCustomer;
  }

  public List<String> getUserVariables() {
    return userVariables;
  }

  public void setUserVariables(List<String> userVariables) {
    this.userVariables = userVariables;
  }

  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public List<String> getReasons() {
    return reasons;
  }

  public void setReasons(List<String> reasons) {
    this.reasons = reasons;
  }

  public String getSuccessUrl() {
    return successUrl;
  }

  public void setSuccessUrl(String successUrl) {
    this.successUrl = successUrl;
  }

  public String getAbortUrl() {
    return abortUrl;
  }

  public void setAbortUrl(String abortUrl) {
    this.abortUrl = abortUrl;
  }

  public String getTimeoutUrl() {
    return timeoutUrl;
  }

  public void setTimeoutUrl(String timeoutUrl) {
    this.timeoutUrl = timeoutUrl;
  }

  public List<String> getNotificationUrls() {
    return notificationUrls;
  }

  public void setNotificationUrls(List<String> notificationUrls) {
    this.notificationUrls = notificationUrls;
  }

  public List<String> getNotificationEmails() {
    return notificationEmails;
  }

  public void setNotificationEmails(List<String> notificationEmails) {
    this.notificationEmails = notificationEmails;
  }

  public boolean isCustomerProtection() {
    return customerProtection;
  }

  public void setCustomerProtection(boolean customerProtection) {
    this.customerProtection = customerProtection;
  }

}
