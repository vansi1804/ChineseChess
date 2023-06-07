package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidMovingPlayerException extends ExceptionCustom {

    public InvalidMovingPlayerException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_MOVING_PLAYER, errors);
    }

}
