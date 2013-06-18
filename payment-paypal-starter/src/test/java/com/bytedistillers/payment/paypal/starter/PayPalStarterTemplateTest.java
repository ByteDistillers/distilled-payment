package com.bytedistillers.payment.paypal.starter;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.bytedistillers.payment.paypal.starter.CartItem;
import com.bytedistillers.payment.paypal.starter.PayPalStarterTemplate;
import com.bytedistillers.payment.paypal.starter.StarterCartFormData;
import com.bytedistillers.payment.paypal.starter.StarterXclickFormData;

public class PayPalStarterTemplateTest {

  private PayPalStarterTemplate template;

  @Before
  public void setup() {
    template = new PayPalStarterTemplate();
  }

  @Test
  public void generateCartHtml() {
    StarterCartFormData formData = generateCartFormData();
    template.setFormTarget("_blank");
    template.setFormData(formData);

    String html = template.generateHtmlForm();
    System.out.println(html);
  }

  private StarterCartFormData generateCartFormData() {
    StarterCartFormData formData = new StarterCartFormData();

    formData.setBusiness("payment@byte-distillers.com");
    formData.setCurrency_code("EUR");
    formData.setCancel_url("http://www.orf.at");
    formData.setSuccess_url("http://www.google.at");

    formData.addItem(new CartItem("haus", BigDecimal.valueOf(123.45)));

    return formData;
  }

  @Test
  public void generateXclickHtml() {
    StarterXclickFormData formData = generateXclickFormData();
    template.setFormTarget("_blank");
    template.setFormData(formData);

    String html = template.generateHtmlForm();
    System.out.println(html);
  }

  private StarterXclickFormData generateXclickFormData() {
    StarterXclickFormData formData = new StarterXclickFormData();

    formData.setBusiness("payment@byte-distillers.com");
    formData.setCurrency_code("EUR");
    formData.setCancel_url("http://www.orf.at");
    formData.setSuccess_url("http://www.google.at");

    formData.setAmount(BigDecimal.valueOf(1.23));
    formData.setItem_number("12ab");
    formData.setItem_name("Meins");
    formData.setCustom("Benutzerdefiniert");
    formData.setHandling(BigDecimal.valueOf(0.05));
    formData.setTax(BigDecimal.valueOf(0.12));
    formData.setInvoice("Rechnungsnr");

    return formData;
  }
}
