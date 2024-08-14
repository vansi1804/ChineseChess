package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;
import java.util.Map;

public class ConflictExceptionCustomize extends BaseExceptionCustomize {

  public ConflictExceptionCustomize(Map<String, Object> errors) {
//    super(ErrorMessage.DATA_ALREADY_EXISTS, errors);
    super(Translator.toLocale("DATA_ALREADY_EXISTS"), errors);
  }
}
