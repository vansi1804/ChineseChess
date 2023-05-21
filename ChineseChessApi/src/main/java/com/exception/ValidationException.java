package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ValidationException extends ExceptionCustom {

    public ValidationException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_DATA, errors);
    }

}
