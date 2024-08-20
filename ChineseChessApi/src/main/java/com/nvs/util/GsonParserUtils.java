package com.nvs.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonParserUtils {

  private static final Gson gson = new Gson();

  public static String parseObjectToString(Object object) {
    return gson.toJson(object);
  }

  public static <T> T parseStringToObject(String json, Class<T> classObject) {
    try {
      return gson.fromJson(json, classObject);
    } catch (JsonSyntaxException e) {
      log.error("Failed to parse JSON string to object: {}", e.getMessage());
      return null;
    }
  }
}
