package com.nvs.config.exception;

import com.nvs.common.ErrorMessage;

public class InternalServerErrorExceptionCustomize extends ExceptionCustomize {

  public InternalServerErrorExceptionCustomize(String errorMessage) {
    super(ErrorMessage.INTERNAL_SERVER_ERROR, errorMessage);
  }
}
