package com.exception;

import java.util.Map;

import com.common.ErrorMessage;

public class InvalidPlayerMovePieceException extends ExceptionCustom {

    public InvalidPlayerMovePieceException(Map<String, Object> errors) {
        super(ErrorMessage.INVALID_PLAYER_MOVE_PIECE, errors);
    }

}