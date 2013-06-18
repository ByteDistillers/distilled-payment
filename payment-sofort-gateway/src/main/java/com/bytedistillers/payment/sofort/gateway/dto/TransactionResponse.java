package com.bytedistillers.payment.sofort.gateway.dto;

public class TransactionResponse {

  private TransactionData transactionData;
  private TransactionErrors transactionErrors;
  private boolean successful;

  public TransactionErrors getTransactionErrors() {
    return transactionErrors;
  }

  public void setTransactionErrors(TransactionErrors transactionErrors) {
    this.transactionErrors = transactionErrors;
    successful = false;
  }

  public TransactionData getTransactionData() {
    return transactionData;
  }

  public void setTransactionData(TransactionData transactionData) {
    this.transactionData = transactionData;
    successful = true;
  }

  public boolean isSuccessful() {
    return successful;
  }
}
