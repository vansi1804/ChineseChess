package com.config.exception;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.common.ErrorMessage;
import com.data.dto.ErrorMessageResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ErrorMessage.INVALID_DATA,
                        Collections.singletonMap(field, defaultMessage),
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageResponseDTO(ErrorMessage.ACCESS_DENIED, "User is inactive",
                        request.getServletPath()));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(LockedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageResponseDTO(ErrorMessage.ACCESS_DENIED, "User is locked",
                        request.getServletPath()));
    }

    @ExceptionHandler(AccessDeniedExceptionCustomize.class)
    public ResponseEntity<?> handleAccessDeniedExceptionCustomize(AccessDeniedExceptionCustomize ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageResponseDTO(ErrorMessage.ACCESS_DENIED, ex.getErrorMessage(),
                        request.getServletPath()));
    }

}
