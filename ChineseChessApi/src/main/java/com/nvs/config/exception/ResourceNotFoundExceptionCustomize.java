package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;
import java.util.Map;

public class ResourceNotFoundExceptionCustomize extends BaseExceptionCustomize {

  public ResourceNotFoundExceptionCustomize(Map<String, Object> errors) {
//    super(ErrorMessage.DATA_NOT_FOUND, errors);
    super(Translator.toLocale("DATA_NOT_FOUND"), errors);
  }

}
