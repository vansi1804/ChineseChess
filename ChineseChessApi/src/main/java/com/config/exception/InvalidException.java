package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidException extends ExceptionCustom {

    public InvalidException(String msg, Map<String, Object> errors) {
        super(msg, errors);
    }

     public InvalidException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_DATA, errors);
    }

}
