package com.config.exception;

import com.common.ErrorMessage;

public class UnauthorizedException extends ExceptionCustomize {

    public UnauthorizedException() {
        super(ErrorMessage.UNAUTHORIZED, "Incorrect phone number or password");
    }

}