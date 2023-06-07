package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ConflictException extends ExceptionCustom {

    public ConflictException(Map<String, Object> errors) {
        super(ErrorMessage.DATA_ALREADY_EXISTS, errors);
    }

}
