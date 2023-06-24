package com.exception;

import java.util.Map;

public class InvalidException extends ExceptionCustom {

    public InvalidException(String msg, Map<String, Object> errors) {
        super(msg, errors);
    }

}