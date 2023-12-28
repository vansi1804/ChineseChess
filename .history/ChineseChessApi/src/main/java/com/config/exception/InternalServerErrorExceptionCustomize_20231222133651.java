package com.config.exception;

import com.common.ErrorMessage;

public class InternalServerErrorExceptionCustomize extends ExceptionCustomize {

  public InternalServerErrorExceptionCustomize(String errorMessage) {
    super(ErrorMessage.INTERNAL_SERVER_ERROR, errorMessage);
  }
}
