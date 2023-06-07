package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidMoveException extends ExceptionCustom {

    public InvalidMoveException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_MOVE, errors);
    }

}