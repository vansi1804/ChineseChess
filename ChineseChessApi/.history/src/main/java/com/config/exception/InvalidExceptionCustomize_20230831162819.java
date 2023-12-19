package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidExceptionCustomize extends ExceptionCustomize {

    public InvalidExceptionCustomize(String msg, Map<String, Object> errors) {
        super(msg, errors);
    }

     public InvalidExceptionCustomize(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_DATA, errors);
    }

}
