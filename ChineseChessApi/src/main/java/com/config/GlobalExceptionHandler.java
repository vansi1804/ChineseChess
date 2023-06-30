package com.config;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.common.ErrorMessage;
import com.data.dto.ErrorMessageResponseDTO;
import com.exception.ConflictException;
import com.exception.InvalidException;
import com.exception.JsonProcessException;
import com.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        // String objectName = ex.getBindingResult().getObjectName();
        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        // Map<String, Object> errors = new LinkedHashMap<>();
        // errors.put("objectName", objectName);

        // String[] fieldNames = field.split("\\.");

        // Map<String, Object> currentError = errors;
        // for (int i = 0; i < fieldNames.length; i++) {
        //     if (i < fieldNames.length - 1) {
        //         LinkedHashMap<String, Object> nestedError = new LinkedHashMap<>();
        //         currentError.put("fieldName", nestedError);
        //         currentError = nestedError;
        //         currentError.put("objectName", fieldNames[i]);
        //     } else {
        //         currentError.put("fieldName", fieldNames[i]);
        //     }
        // }
    
        // errors.put("message", defaultMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ErrorMessage.INVALID_DATA, Collections.singletonMap(field, defaultMessage),
                        ((ServletWebRequest) request).getRequest().getServletPath()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(JsonProcessException.class)
    public ResponseEntity<?> handleJsonProcessException(JsonProcessException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<?> handleInvalidException(InvalidException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

}
