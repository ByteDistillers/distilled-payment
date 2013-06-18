package com.bytedistillers.payment.sofort.gateway.dto;

public class TransactionData {

  private String transactionId;
  private String paymentUrl;

  public TransactionData() {
  }

  public TransactionData(String transactionId, String paymentUrl) {
    this.transactionId = transactionId;
    this.paymentUrl = paymentUrl;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getPaymentUrl() {
    return paymentUrl;
  }

  public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

}
