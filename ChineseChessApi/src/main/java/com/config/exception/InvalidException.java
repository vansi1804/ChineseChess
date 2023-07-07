package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidException extends ExceptionCustomize {

    public InvalidException(String msg, Map<String, Object> errors) {
        super(msg, errors);
    }

     public InvalidException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_DATA, errors);
    }

}
