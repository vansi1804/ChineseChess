package com.nvs.config.exception;

import com.nvs.common.ErrorMessage;
import java.util.Map;

public class ResourceNotFoundExceptionCustomize extends ExceptionCustomize{

   public ResourceNotFoundExceptionCustomize(Map<String, Object> errors){
      super(ErrorMessage.DATA_NOT_FOUND, errors);
   }

}
