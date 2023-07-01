package com.exception;

import java.util.Collections;

import com.common.ErrorMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonProcessException extends ExceptionCustom {

    public JsonProcessException(JsonProcessingException ex) {
        super(ErrorMessage.INVALID_JSON_PAYLOAD, extractErrorMessage(ex));
    }

    private static Object extractErrorMessage(JsonProcessingException ex) {
        String fieldName = getFieldError(ex);
        return fieldName == null
        ? ex.getOriginalMessage()
        : Collections.singletonMap(fieldName, "UNRECOGNIZED");
    }

    private static String getFieldError(JsonProcessingException e) {
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