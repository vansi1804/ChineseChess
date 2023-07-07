package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ConflictException extends ExceptionCustomize {

    public ConflictException(Map<String, Object> errors) {
        super(ErrorMessage.DATA_ALREADY_EXISTS, errors);
    }

}
