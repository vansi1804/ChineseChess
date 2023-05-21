package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ResourceNotFoundException extends ExceptionCustom {

    public ResourceNotFoundException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_DATA, errors);
    }

}