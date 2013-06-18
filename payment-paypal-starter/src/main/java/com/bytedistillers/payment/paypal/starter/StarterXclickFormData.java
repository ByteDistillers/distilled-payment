package com.bytedistillers.payment.paypal.starter;

import java.math.BigDecimal;

public class StarterXclickFormData extends FormData {

  private String cmd = "_xclick";

  private BigDecimal amount;
  private BigDecimal handling;
  private BigDecimal tax;
  private String invoice;
  private String item_name;
  private String item_number;

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getItem_name() {
    return item_name;
  }

  public void setItem_name(String item_name) {
    this.item_name = item_name;
  }

  public String getItem_number() {
    return item_number;
  }

  public void setItem_number(String item_number) {
    this.item_number = item_number;
  }

  public String getCmd() {
    return cmd;
  }

  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  public BigDecimal getHandling() {
    return handling;
  }

  public void setHandling(BigDecimal handling) {
    this.handling = handling;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public String getInvoice() {
    return invoice;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

}
