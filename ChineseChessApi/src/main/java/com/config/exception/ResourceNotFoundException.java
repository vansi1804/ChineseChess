package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ResourceNotFoundException extends ExceptionCustomize {

    public ResourceNotFoundException(Map<String, Object> errors) {
        super(ErrorMessage.DATA_NOT_FOUND, errors);
    }

}