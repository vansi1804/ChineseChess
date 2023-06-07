package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class OpponentTurnException extends ExceptionCustom {

    public OpponentTurnException(Map<String, Object> errors) {
        super(ErrorMessage.OPPONENT_TURN, errors);
    }

}