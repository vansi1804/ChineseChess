package com.config.exception;

import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.common.ErrorMessage;
import com.data.dto.ErrorMessageResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleInternalServerErrorException(
            InternalServerErrorException ex, HttpServletRequest request) {

        logger.error("Error occurred while retrieving ", ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessageResponseDTO(ErrorMessage.SERVER_ERROR, null, request.getServletPath()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
                
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleConflictException(
            ConflictException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(JsonProcessException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleJsonProcessException(
            JsonProcessException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        FieldError fieldError = ex.getBindingResult().getFieldErrors().get(0);
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();

        Object errors = Collections.singletonMap(field, defaultMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), errors, request.getServletPath()));
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleInvalidException(
            InvalidException ex, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler({
            DisabledException.class,
            LockedException.class,
            ExpiredJwtException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErrorMessageResponseDTO> handleForbiddenException(
            Exception ex, HttpServletRequest request) {

        String error;

        if (ex instanceof DisabledException) {
            error = ErrorMessage.DISABLE_USER;
        } else if (ex instanceof LockedException) {
            error = ErrorMessage.LOCKED_USER;
        } else if (ex instanceof ExpiredJwtException) {
            error = ErrorMessage.EXPIRED_TOKEN;
        } else {
            error = ErrorMessage.NOT_ENOUGH_PERMISSION;
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorMessageResponseDTO(ErrorMessage.ACCESS_DENIED, error, request.getServletPath()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorMessageResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }
}
