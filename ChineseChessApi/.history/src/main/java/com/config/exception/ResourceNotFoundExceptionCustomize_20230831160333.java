package com.config.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class ResourceNotFoundExceptionCustomize extends ExceptionCustomize {

    public ResourceNotFoundExceptionCustomize(Map<String, Object> errors) {
        super(ErrorMessage.DATA_NOT_FOUND, errors);
    }

}