package com.exception;

import java.util.Map;

public class ResourceNotFoundException extends ExceptionCustom {

    public ResourceNotFoundException(Map<String, Object> errors) {
        super(errors, "Data not found!");
    }

}