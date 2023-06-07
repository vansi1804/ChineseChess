package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class DeadPieceException extends ExceptionCustom {

    public DeadPieceException(Map<String, Object> errors) {
        super(ErrorMessage.DEAD_PIECE, errors);
    }

}