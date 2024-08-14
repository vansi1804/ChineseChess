package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;

public class InternalServerErrorExceptionCustomize extends BaseExceptionCustomize {

  public InternalServerErrorExceptionCustomize(String errorMessage) {
//    super(ErrorMessage.INTERNAL_SERVER_ERROR, errorMessage);
    super(Translator.toLocale("INTERNAL_SERVER_ERROR"), errorMessage);
  }

}
