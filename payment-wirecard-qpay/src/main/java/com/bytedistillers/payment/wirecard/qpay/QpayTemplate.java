package com.bytedistillers.payment.wirecard.qpay;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The QpayTemplate acts as your one-stop-shop for all things needed to generate a hidden html-form.<br/>
 * This hidden form is then used to submit your order specific data to the qpay server.
 * 
 * @author Martin Gutenbrunner (martin.gutenbrunner@pagu.at)
 * 
 */
public class QpayTemplate {

  private static final Logger logger = LoggerFactory.getLogger(QpayTemplate.class);

  private String secret = "B8AKTPWBRMNBV455FG6M2DANE99WU2";
  private String formActionUrl = "https://secure.wirecard-cee.com/qpay/init.php";
  private String formTarget = "_blank";
  private String formName = "qpayHiddenForm";
  private QpayFormData formData;

  public String generateHtmlForm() {
    StringBuilder result = new StringBuilder("<form action=\"" + formActionUrl + "\" method=\"POST\" target=\"" + formTarget + "\" name=\"" + formName + "\">\r\n");

    boolean fingerprintOrderAdded = false;
    Method[] methods = QpayFormData.class.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        Object value = getReturnValue(method);
        if (value != null) {
          String fieldName = method.getName().replaceFirst("get", "");
          fieldName = StringUtils.uncapitalize(fieldName);
          if (StringUtils.equals(fieldName, "class")) {
            continue;
          }
          result.append(createInput(fieldName, value.toString()));

          if (StringUtils.equals("requestFingerprintOrder", fieldName)) {
            fingerprintOrderAdded = true;
          }
        }
      }
    }

    result.append(createInput("requestFingerprint", generateFingerprint()));

    if (!fingerprintOrderAdded) {
      result.append(createInput("requestFingerprintOrder", formData.getRequestFingerprintOrder()));
    }

    result.append("</form>\r\n");

    return result.toString();
  }

  private String createInput(String name, String value) {
    return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />\r\n";
  }

  /**
   * This generates the required value for the fingerprintOrder parameter.<br/>
   * This method is also called by the generateFormHtml() method.
   * 
   * @return value for the fingerprintOrder parameter in the form
   */
  public String generateFingerprint() {
    if (formData == null) {
      throw new RuntimeException("No FormData specified! Please call QpayTemplate.setFormData(formData) first!");
    }
    MessageDigest md5Digest = getMd5Digest();

    StringBuilder seed = new StringBuilder();
    String requestFingerprintOrder = formData.getRequestFingerprintOrder();

    if (StringUtils.isBlank(requestFingerprintOrder)) {
      requestFingerprintOrder = appendAllFilledFields(seed);

      formData.setRequestFingerprintOrder(requestFingerprintOrder);
    } else {
      appendDefinedFields(seed);
    }

    byte[] hash = md5Digest.digest(getUtf8Bytes(seed));

    String fingerprint = getHashString(hash);

    return fingerprint;
  }

  private String appendAllFilledFields(StringBuilder seed) {
    StringBuilder fpOrder = new StringBuilder();
    Method[] methods = QpayFormData.class.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        Object value = getReturnValue(method);
        if (value != null) {
          String fieldName = method.getName().replaceFirst("get", "");
          fieldName = StringUtils.uncapitalize(fieldName);
          if (StringUtils.equals(fieldName, "class")) {
            continue;
          }
          fpOrder.append(fieldName + ",");
          appendFieldValueToSeed(seed, fieldName);
        }
      }
    }
    fpOrder.append("secret,");
    seed.append(secret);

    fpOrder.append("requestFingerprintOrder");
    seed.append(fpOrder.toString());

    return fpOrder.toString();
  }

  private void appendDefinedFields(StringBuilder seed) {
    String[] names = formData.getRequestFingerprintOrder().split(",");
    for (String name : names) {
      if (StringUtils.equalsIgnoreCase("secret", name)) {
        seed.append(secret);
      } else {
        appendFieldValueToSeed(seed, name);
      }
    }
  }

  private void appendFieldValueToSeed(StringBuilder seed, String name) {
    Object value = getValueFromField(name);

    if (value != null) {
      seed.append(value.toString());
    }
  }

  private byte[] getUtf8Bytes(StringBuilder seed) {
    try {
      return seed.toString().getBytes("UTF-8");
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private String getHashString(byte[] hash) {
    StringBuffer sb = new StringBuffer();
    String hex;
    for (byte element : hash) {
      hex = Integer.toHexString(0xFF & element);
      switch (hex.length()) {
        case 0:
          sb.append("00");
          break;
        case 1:
          sb.append("0");
          sb.append(hex);
          break;
        case 2:
          sb.append(hex);
          break;
        default:
          throw new RuntimeException("illegal hash");
      }
    }
    return sb.toString();
  }

  private Object getValueFromField(String name) {
    Object value;
    try {
      PropertyDescriptor pd = new PropertyDescriptor(name, QpayFormData.class);
      Method method = pd.getReadMethod();
      value = getReturnValue(method);
    } catch (IntrospectionException e) {
      logger.error(e.getMessage(), e);
      //        throw new RuntimeException("Unable to find getter for field {}. Please check");
      value = null;
    }
    return value;
  }

  private Object getReturnValue(Method method) {
    Object result;
    try {
      result = method.invoke(formData);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      result = null;
    }
    return result;
  }

  private MessageDigest getMd5Digest() {
    try {
      MessageDigest md5Digest = MessageDigest.getInstance("MD5");
      return md5Digest;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public QpayFormData getFormData() {
    return formData;
  }

  public void setFormData(QpayFormData formData) {
    this.formData = formData;
  }

  public String getFormName() {
    return formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public String getFormActionUrl() {
    return formActionUrl;
  }

  public void setFormActionUrl(String formActionUrl) {
    this.formActionUrl = formActionUrl;
  }

  public String getFormTarget() {
    return formTarget;
  }

  public void setFormTarget(String formTarget) {
    this.formTarget = formTarget;
  }
}
