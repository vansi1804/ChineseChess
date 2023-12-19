package com.config.exception;

import com.common.ErrorMessage;

public class InternalServerErrorExceptionCustomize extends ExceptionCustomize {

    public InternalServerErrorExceptionCustomize(String errorMessage) {
        super(ErrorMessage.SERVER_ERROR, errorMessage);
    }

}