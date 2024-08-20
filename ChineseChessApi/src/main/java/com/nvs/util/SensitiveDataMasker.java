package com.nvs.util;

import java.util.List;

public class SensitiveDataMasker {

  private static final List<Class<?>> EXCLUDED_CLASSES = List.of(String.class);

  public static String maskSensitiveData(Object object) {
//    if (object == null) {
//      return "null";
//    }
//
//    StringBuilder maskedData = new StringBuilder();
//
//    Field[] fields = object.getClass().getDeclaredFields();
//    for (Field field : fields) {
//      field.setAccessible(true);
//      try {
//        Object value = field.get(object);
//        if (field.isAnnotationPresent(SensitiveData.class)) {
//          if (EXCLUDED_CLASSES.contains(field.getType())) {
//            maskedData.append(field.getName()).append(": ").append("****").append("\n");
//          } else if (value instanceof String) {
//            maskedData.append(field.getName()).append(": ").append(maskValue((String) value))
//                .append("\n");
//          } else {
//            maskedData.append(field.getName()).append(": ").append("****").append("\n");
//          }
//        } else {
//          maskedData.append(field.getName()).append(": ").append(value).append("\n");
//        }
//      } catch (IllegalAccessException e) {
//        maskedData.append(field.getName()).append(": ").append("error").append("\n");
//      }
//    }
//
//    return maskedData.toString();

    return null;
  }

  private static String maskValue(String value) {
    // Mask toàn bộ ngoại trừ 4 ký tự cuối
    return value.replaceAll(".(?=.{4})", "*");
  }
}
