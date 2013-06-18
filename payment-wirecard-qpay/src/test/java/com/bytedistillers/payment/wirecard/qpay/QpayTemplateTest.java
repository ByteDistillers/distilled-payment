package com.bytedistillers.payment.wirecard.qpay;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class QpayTemplateTest {

  private QpayTemplate template;

  @Before
  public void setup() {

    QpayFormData staticFormData = new QpayFormData();
    staticFormData.setOrderDescription("JUnit Order Description " + System.currentTimeMillis());
    staticFormData.setCurrency("EUR");
    staticFormData.setLanguage("de");
    staticFormData.setPaymenttype(QpayPaymentType.CCARD.toString());
    staticFormData.setSuccessURL("http://localhost:7180/hawker-web/qpay/success.html");
    staticFormData.setCancelURL("http://localhost:7180/hawker-web/qpay/cancel.html");
    staticFormData.setFailureURL("http://localhost:7180/hawker-web/qpay/failure.html");
    staticFormData.setServiceURL("http://localhost:7180/hawker-web/qpay/service.html");
    staticFormData.setRequestFingerprintOrder("customerID,currency,language,secret,amount,successURL,orderDescription,requestFingerprintOrder");

    template = new QpayTemplate(staticFormData);
  }

  @Test
  public void generateFingerprint() {
    QpayFormData dynamicFormData = new QpayFormData();
    dynamicFormData.setAmount(BigDecimal.valueOf(123.4567));

    String fingerprint = template.generateFingerprint(dynamicFormData);
    System.out.println(fingerprint);
  }

  @Test
  public void generateHtml() {
    QpayFormData dynamicFormData = new QpayFormData();
    dynamicFormData.setAmount(BigDecimal.valueOf(123.4567));

    String html = template.generateHtmlForm(dynamicFormData);
    System.out.println(html);
  }
}
