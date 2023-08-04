package com.config.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.common.ErrorMessage;
import com.config.validation.impl.PlayBoardValidator;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlayBoardValidator.class)
public @interface Validator {

    String message() default ErrorMessage.INVALID_DATA;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
