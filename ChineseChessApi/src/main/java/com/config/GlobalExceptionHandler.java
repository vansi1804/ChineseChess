package com.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.data.dto.ErrorMessageResponseDTO;
import com.exception.ConflictException;
import com.exception.DeadPieceException;
import com.exception.InvalidMoveException;
import com.exception.InvalidMovingPlayerException;
import com.exception.InvalidPlayerMovePieceException;
import com.exception.OpponentTurnException;
import com.exception.ValidationException;
import com.exception.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), errors, request.getServletPath()));
    }

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<?> handleInvalidMoveException(InvalidMoveException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(DeadPieceException.class)
    public ResponseEntity<?> handleDeadPieceException(DeadPieceException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(OpponentTurnException.class)
    public ResponseEntity<?> handleOpponentTurnException(OpponentTurnException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(InvalidMovingPlayerException.class)
    public ResponseEntity<?> handleInvalidMovingPlayerException(InvalidMovingPlayerException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

    @ExceptionHandler(InvalidPlayerMovePieceException.class)
    public ResponseEntity<?> handleInvalidPlayerMovePieceException(InvalidPlayerMovePieceException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageResponseDTO(ex.getMessage(), ex.getErrors(), request.getServletPath()));
    }

}
