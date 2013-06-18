package com.bytedistillers.payment.paypal.starter;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class FormData {

  protected String business;

  private String notify_url;
  @XmlAttribute(name = "cancel_return")
  private String cancel_url; //cancel_return
  @XmlAttribute(name = "return")
  private String success_url; //return
  private String currency_code;
  private String custom;

  public String getBusiness() {
    return business;
  }

  public void setBusiness(String business) {
    this.business = business;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public String getCancel_url() {
    return cancel_url;
  }

  public void setCancel_url(String cancel_url) {
    this.cancel_url = cancel_url;
  }

  public String getSuccess_url() {
    return success_url;
  }

  public void setSuccess_url(String success_url) {
    this.success_url = success_url;
  }

  public String getCurrency_code() {
    return currency_code;
  }

  public void setCurrency_code(String currency_code) {
    this.currency_code = currency_code;
  }

  public String getCustom() {
    return custom;
  }

  public void setCustom(String custom) {
    this.custom = custom;
  }

}