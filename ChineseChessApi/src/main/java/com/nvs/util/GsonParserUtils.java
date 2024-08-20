package com.nvs.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonParserUtils {

  public static String parseObjectToString(Object object) {
    return new Gson().toJson(object);
  }

  public static <T> T parseStringToObject(String json, Class<T> classObject) {
    try {
      return new Gson().fromJson(json, classObject);
    } catch (Exception e) {
      return null;
    }
  }
}

