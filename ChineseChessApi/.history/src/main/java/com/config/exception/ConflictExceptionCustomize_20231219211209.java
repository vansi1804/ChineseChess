package com.config.exception;

import com.common.ErrorMessage;
import java.util.Map;

public class ConflictExceptionCustomize extends ExceptionCustomize {

  public ConflictExceptionCustomize(Map<String, Object> errors) {
    super(ErrorMessage.DATA_ALREADY_EXISTS, errors);
  }
}
