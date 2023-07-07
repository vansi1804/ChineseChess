package com.config.exception;

import java.io.IOException;
import java.util.Collections;

import com.common.ErrorMessage;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonProcessException extends ExceptionCustomize {

    public JsonProcessException(IOException ex) {
        super(ErrorMessage.INVALID_JSON_PAYLOAD, extractErrorMessage(ex));
    }

    private static Object extractErrorMessage(IOException ex) {
        String fieldName = getFieldError(ex);
        return fieldName == null
        ? ex.getMessage()
        : Collections.singletonMap(fieldName, "UNRECOGNIZED");
    }

    private static String getFieldError(IOException e) {
        String fieldName = null;
        if (e instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) e;
            for (JsonMappingException.Reference reference : jsonMappingException.getPath()) {
                if (reference.getFieldName() != null) {
                    fieldName = reference.getFieldName();
                }
            }
        }
        return fieldName;
    }
    
}