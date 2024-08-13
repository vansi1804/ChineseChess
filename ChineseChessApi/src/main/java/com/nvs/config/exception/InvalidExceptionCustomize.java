package com.nvs.config.exception;

import com.nvs.common.ErrorMessage;
import java.util.Map;

public class InvalidExceptionCustomize extends ExceptionCustomize {

  public InvalidExceptionCustomize(String msg, Map<String, Object> errors) {
    super(msg, errors);
  }

  public InvalidExceptionCustomize(Map<String, Object> errors) {
    super(ErrorMessage.INVALID_DATA, errors);
  }

}
