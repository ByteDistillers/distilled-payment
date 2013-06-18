package com.bytedistillers.payment.wirecard.qpay;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bytedistillers.payment.common.form.FormDataMergeUtil;
import com.bytedistillers.payment.common.form.FormGeneratorUtil;

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
  private QpayFormData staticFormData;

  private FormGeneratorUtil formGeneratorUtil = new FormGeneratorUtil();
  private FormDataMergeUtil<QpayFormData> formDataMergeUtil = new FormDataMergeUtil<QpayFormData>();

  public QpayTemplate() {
  }

  public QpayTemplate(QpayFormData staticFormData) {
    this.staticFormData = staticFormData;
  }

  public String generateHtmlForm(QpayFormData dynamicFormData) {
    QpayFormData mergedFormData = formDataMergeUtil.merge(dynamicFormData, staticFormData, QpayFormData.class);

    String toAppend = formGeneratorUtil.generateFormStartTag(formActionUrl, formTarget, formName);
    StringBuilder result = new StringBuilder(toAppend);

    boolean fingerprintOrderAdded = false;
    Method[] methods = QpayFormData.class.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        Object value = getReturnValue(method, mergedFormData);
        if (value != null) {
          String fieldName = method.getName().replaceFirst("get", "");
          fieldName = StringUtils.uncapitalize(fieldName);
          if (StringUtils.equals(fieldName, "class")) {
            continue;
          }
          result.append(formGeneratorUtil.createInput(fieldName, value.toString()));

          if (StringUtils.equals("requestFingerprintOrder", fieldName)) {
            fingerprintOrderAdded = true;
          }
        }
      }
    }

    result.append(formGeneratorUtil.createInput("requestFingerprint", generateFingerprint(mergedFormData)));

    if (!fingerprintOrderAdded) {
      result.append(formGeneratorUtil.createInput("requestFingerprintOrder", mergedFormData.getRequestFingerprintOrder()));
    }

    result.append(formGeneratorUtil.generateFormEndTag());

    return result.toString();
  }

  /**
   * This generates the required value for the fingerprintOrder parameter.<br/>
   * This method is also called by the generateFormHtml() method.
   * 
   * @return value for the fingerprintOrder parameter in the form
   */
  public String generateFingerprint(QpayFormData dynamicFormData) {
    QpayFormData mergedFormData = formDataMergeUtil.merge(dynamicFormData, staticFormData, QpayFormData.class);
    MessageDigest md5Digest = getMd5Digest();

    StringBuilder seed = new StringBuilder();
    String requestFingerprintOrder = mergedFormData.getRequestFingerprintOrder();

    if (StringUtils.isBlank(requestFingerprintOrder)) {
      requestFingerprintOrder = appendAllFilledFields(seed, mergedFormData);

      mergedFormData.setRequestFingerprintOrder(requestFingerprintOrder);
    } else {
      appendDefinedFields(seed, mergedFormData);
    }

    byte[] hash = md5Digest.digest(getUtf8Bytes(seed));

    String fingerprint = getHashString(hash);

    return fingerprint;
  }

  private String appendAllFilledFields(StringBuilder seed, QpayFormData formData) {
    StringBuilder fpOrder = new StringBuilder();
    Method[] methods = QpayFormData.class.getMethods();
    for (Method method : methods) {
      if (method.getName().startsWith("get")) {
        Object value = getReturnValue(method, formData);
        if (value != null) {
          String fieldName = method.getName().replaceFirst("get", "");
          fieldName = StringUtils.uncapitalize(fieldName);
          if (StringUtils.equals(fieldName, "class")) {
            continue;
          }
          fpOrder.append(fieldName + ",");
          appendFieldValueToSeed(seed, fieldName, formData);
        }
      }
    }
    fpOrder.append("secret,");
    seed.append(secret);

    fpOrder.append("requestFingerprintOrder");
    seed.append(fpOrder.toString());

    return fpOrder.toString();
  }

  private void appendDefinedFields(StringBuilder seed, QpayFormData formData) {
    String[] names = formData.getRequestFingerprintOrder().split(",");
    for (String name : names) {
      if (StringUtils.equalsIgnoreCase("secret", name)) {
        seed.append(secret);
      } else {
        appendFieldValueToSeed(seed, name, formData);
      }
    }
  }

  private void appendFieldValueToSeed(StringBuilder seed, String name, QpayFormData formData) {
    Object value = getValueFromField(name, formData);

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

  private Object getValueFromField(String name, QpayFormData formData) {
    Object value;
    try {
      PropertyDescriptor pd = new PropertyDescriptor(name, QpayFormData.class);
      Method method = pd.getReadMethod();
      value = getReturnValue(method, formData);
    } catch (IntrospectionException e) {
      logger.error(e.getMessage(), e);
      //        throw new RuntimeException("Unable to find getter for field {}. Please check");
      value = null;
    }
    return value;
  }

  private Object getReturnValue(Method method, QpayFormData formData) {
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

  public QpayFormData getStaticFormData() {
    return staticFormData;
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
