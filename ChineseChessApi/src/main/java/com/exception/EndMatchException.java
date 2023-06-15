package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class EndMatchException extends ExceptionCustom {

    public EndMatchException(Map<String, Object> errors) {
        super(ErrorMessage.END_MATCH, errors);
    }

}