package com.config.exception;

import com.common.ErrorMessage;

public class InternalServerErrorException extends ExceptionCustomize {

    public InternalServerErrorException(String errorMessage) {
        super(ErrorMessage.SERVER_ERROR, errorMessage);
    }

}