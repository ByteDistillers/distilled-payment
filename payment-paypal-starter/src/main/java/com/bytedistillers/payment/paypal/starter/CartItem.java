package com.bytedistillers.payment.paypal.starter;

import java.math.BigDecimal;
import java.util.List;

public class CartItem {

  private BigDecimal amount;
  private String handling;
  private String item_name;
  private String item_number;
  private List<OptionalParameter> optionalParameters;
  private Integer quantity;
  private BigDecimal shipping;
  private BigDecimal shipping_2;
  private BigDecimal tax;

  public CartItem() {
  }

  public CartItem(String item_name, BigDecimal amount) {
    this.item_name = item_name;
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getHandling() {
    return handling;
  }

  public void setHandling(String handling) {
    this.handling = handling;
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

  public List<OptionalParameter> getOptionalParameters() {
    return optionalParameters;
  }

  public void setOptionalParameters(List<OptionalParameter> optionalParameters) {
    this.optionalParameters = optionalParameters;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getShipping() {
    return shipping;
  }

  public void setShipping(BigDecimal shipping) {
    this.shipping = shipping;
  }

  public BigDecimal getShipping_2() {
    return shipping_2;
  }

  public void setShipping_2(BigDecimal shipping_2) {
    this.shipping_2 = shipping_2;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

}
