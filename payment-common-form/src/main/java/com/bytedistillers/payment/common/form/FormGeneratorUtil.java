package com.bytedistillers.payment.common.form;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlAttribute;

public class FormGeneratorUtil {

  public String generateFormStartTag(String action, String target, String name) {
    StringBuilder result = new StringBuilder("<form action=\"" + action + "\" method=\"post\"");

    if (target != null) {
      result.append(" target=\"" + target + "\"");
    }

    if (name != null) {
      result.append(" name=\"" + name + "\"");
    }

    result.append(">\r\n");

    return result.toString();
  }

  public String generateFormEndTag() {
    return "</form>\r\n";
  }

  public String generateFieldValues(Class<?> clazz, Object object) {
    return generateFieldValues(clazz, object, "");
  }

  public String generateFieldValues(Class<?> clazz, Object object, String fieldNameAppend) {
    StringBuilder result = new StringBuilder();

    if (clazz.getSuperclass() != Object.class) {
      generateFieldValues(clazz.getSuperclass(), object, fieldNameAppend);
    }

    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      String value = handleField(field, object);
      if (value == null) {
        continue;
      }
      String inputName;
      if (field.isAnnotationPresent(XmlAttribute.class)) {
        XmlAttribute annotation = field.getAnnotation(XmlAttribute.class);
        inputName = annotation.name();
      } else if (field.isAnnotationPresent(XmlIgnore.class)) {
        continue;
      } else {
        inputName = field.getName();
      }
      result.append(createInput(inputName + fieldNameAppend, value));
    }

    return result.toString();
  }

  private String handleField(Field field, Object object) {
    boolean accessible = field.isAccessible();
    if (!accessible) {
      field.setAccessible(true);
    }
    try {
      Object valueObject = field.get(object);
      field.setAccessible(accessible);

      if (valueObject == null) {
        return null;
      }
      String value = valueObject.toString();

      return value;
    } catch (Exception e) {
      return null;
    }
  }

  public String createInput(String name, String value) {
    return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\" />\r\n";
  }
}
