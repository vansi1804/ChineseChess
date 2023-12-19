package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ConflictExceptionCustomize extends ExceptionCustomize {

    public ConflictExceptionCustomize(Map<String, Object> errors) {
        super(ErrorMessage.DATA_ALREADY_EXISTS, errors);
    }

}
