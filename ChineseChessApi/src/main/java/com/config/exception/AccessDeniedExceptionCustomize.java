package com.config.exception;

import org.springframework.security.access.AccessDeniedException;

import com.common.ErrorMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccessDeniedExceptionCustomize extends AccessDeniedException {

    private String errorMessage;

    public AccessDeniedExceptionCustomize() {
        super(ErrorMessage.ACCESS_DENIED);
        this.errorMessage = "Not enough permission";
    }

    public AccessDeniedExceptionCustomize(String errorMessage) {
        super(ErrorMessage.ACCESS_DENIED);
        this.errorMessage = errorMessage;
    }

}