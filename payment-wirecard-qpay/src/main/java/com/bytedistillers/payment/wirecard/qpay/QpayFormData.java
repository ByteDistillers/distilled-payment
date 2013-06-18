package com.bytedistillers.payment.wirecard.qpay;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class QpayFormData {

  private String customerID = "D200001";
  private BigDecimal amount;
  private String currency;
  private String paymenttype = "SELECT";
  private String language;
  private String orderDescription;
  private String successURL;
  private String cancelURL;
  private String failureURL;
  private String serviceURL;
  private String requestFingerprintOrder;
  private String requestFingerprint;

  public String getCustomerID() {
    return customerID;
  }

  public void setCustomerID(String customerID) {
    this.customerID = customerID;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getPaymenttype() {
    return paymenttype;
  }

  public void setPaymenttype(String paymenttype) {
    this.paymenttype = paymenttype;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getOrderDescription() {
    return orderDescription;
  }

  public void setOrderDescription(String orderDescription) {
    this.orderDescription = orderDescription;
  }

  public String getSuccessURL() {
    return successURL;
  }

  public void setSuccessURL(String successURL) {
    this.successURL = successURL;
  }

  public String getCancelURL() {
    return cancelURL;
  }

  public void setCancelURL(String cancelURL) {
    this.cancelURL = cancelURL;
  }

  public String getFailureURL() {
    return failureURL;
  }

  public void setFailureURL(String failureURL) {
    this.failureURL = failureURL;
  }

  public String getServiceURL() {
    return serviceURL;
  }

  public void setServiceURL(String serviceURL) {
    this.serviceURL = serviceURL;
  }

  public String getRequestFingerprintOrder() {
    return requestFingerprintOrder;
  }

  public void setRequestFingerprintOrder(String requestFingerprintOrder) {
    this.requestFingerprintOrder = requestFingerprintOrder;
  }

  public String getRequestFingerprint() {
    return requestFingerprint;
  }

  public void setRequestFingerprint(String requestFingerprint) {
    this.requestFingerprint = requestFingerprint;
  }

}
