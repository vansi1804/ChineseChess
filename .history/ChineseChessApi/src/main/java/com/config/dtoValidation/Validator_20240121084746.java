package com.config.dtoValidation;

import com.common.ErrorMessage;
import com.config.dtoValidation.impl.PlayBoardDTOValidatorIml;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
  validatedBy = {
    PlayBoardDTOValidatorIml.class,
    MatchOthersInfoCreationDTOValidatorIml.class,
  }
)
public @interface Validator {
  String message() default ErrorMessage.INVALID_DATA;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends ConstraintValidator<?, ?>>[] value();
}
