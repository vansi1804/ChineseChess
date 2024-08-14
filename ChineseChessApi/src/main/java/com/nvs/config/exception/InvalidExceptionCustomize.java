package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;
import java.util.Map;

public class InvalidExceptionCustomize extends BaseExceptionCustomize {

  public InvalidExceptionCustomize(String msg, Map<String, Object> errors) {
    super(msg, errors);
  }

  public InvalidExceptionCustomize(Map<String, Object> errors) {
//    super(ErrorMessage.INVALID_DATA, errors);
    super(Translator.toLocale("INVALID_DATA"), errors);
  }

}
