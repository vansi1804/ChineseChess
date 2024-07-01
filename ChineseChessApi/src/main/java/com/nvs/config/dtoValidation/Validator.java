package com.nvs.config.dtoValidation;

import com.nvs.common.ErrorMessage;
import com.nvs.config.dtoValidation.impl.PlayBoardDTOValidatorIml;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PlayBoardDTOValidatorIml.class})
public @interface Validator{

   String message() default ErrorMessage.INVALID_DATA;

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   Class<? extends ConstraintValidator<?, ?>>[] value();
   
}
