package com.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

public class ValidationDataUtil<T> {
    
    public static <T> Map<String, Object> errors(T dto) {
        Set<ConstraintViolation<T>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(dto);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            // Custom error details from the violations
            for (ConstraintViolation<T> violation : violations) {
                errors.put("field", violation.getPropertyPath().toString());
                errors.put("message", violation.getMessage());
            }
            return errors;
        }
        return null;
    }
}
