package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;

public class UnauthorizedExceptionCustomize extends BaseExceptionCustomize {

  public UnauthorizedExceptionCustomize() {
    super(Translator.toLocale("UNAUTHORIZED"));
  }

}
