package com.config.exception;

import com.common.ErrorMessage;
import java.util.Map;

public class ResourceNotFoundExceptionCustomize extends ExceptionCustomize {

  public ResourceNotFoundExceptionCustomize(Map<String, Object> errors) {
    super(ErrorMessage.DATA_NOT_FOUND, errors);
  }
}
