package com.bytedistillers.payment.wirecard.qpay;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.bytedistillers.payment.wirecard.qpay.QpayFormData;
import com.bytedistillers.payment.wirecard.qpay.QpayPaymentType;
import com.bytedistillers.payment.wirecard.qpay.QpayTemplate;

public class QpayTemplateTest {

  private QpayTemplate template;

  @Before
  public void setup() {
    template = new QpayTemplate();

    QpayFormData formData = new QpayFormData();
    formData.setAmount(BigDecimal.valueOf(123.4567));
    formData.setOrderDescription("JUnit Order Description " + System.currentTimeMillis());
    formData.setCurrency("EUR");
    formData.setLanguage("de");
    formData.setPaymenttype(QpayPaymentType.CCARD.toString());
    formData.setSuccessURL("http://localhost:7180/hawker-web/qpay/success.html");
    formData.setCancelURL("http://localhost:7180/hawker-web/qpay/cancel.html");
    formData.setFailureURL("http://localhost:7180/hawker-web/qpay/failure.html");
    formData.setServiceURL("http://localhost:7180/hawker-web/qpay/service.html");
    formData.setRequestFingerprintOrder("customerID,currency,language,secret,amount,successURL,orderDescription,requestFingerprintOrder");

    template.setFormData(formData);
  }

  @Test
  public void generateFingerprint() {
    String fingerprint = template.generateFingerprint();
    System.out.println(fingerprint);
  }

  @Test
  public void generateHtml() {
    String html = template.generateHtmlForm();
    System.out.println(html);
  }
}
