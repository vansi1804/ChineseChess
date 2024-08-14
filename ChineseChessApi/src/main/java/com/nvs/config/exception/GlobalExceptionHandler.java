package com.nvs.config.exception;

import com.nvs.config.i18nMessage.Translator;
import com.nvs.data.dto.ErrorMessageResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InternalServerErrorExceptionCustomize.class)
  public ResponseEntity<ErrorMessageResponseDTO> handleInternalServerErrorException(
      HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorMessageResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            Translator.toLocale("INTERNAL_SERVER_ERROR"), null, request.getServletPath()));
  }

  @ExceptionHandler(ResourceNotFoundExceptionCustomize.class)
  public ResponseEntity<ErrorMessageResponseDTO> handleNotFoundException(
      ResourceNotFoundExceptionCustomize ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorMessageResponseDTO(HttpStatus.NOT_FOUND.value(),
            Translator.toLocale("DATA_NOT_FOUND"), ex.getErrors(), request.getServletPath()));
  }

  @ExceptionHandler(ConflictExceptionCustomize.class)
  public ResponseEntity<ErrorMessageResponseDTO> handleConflictException(
      ConflictExceptionCustomize ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ErrorMessageResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage(), ex.getErrors(),
            request.getServletPath()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessageResponseDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    Map<String, Object> errors = new HashMap<>();

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      String field = fieldError.getField();
      String defaultMessage = fieldError.getDefaultMessage();

      // Resolve the default message to the localized message
      String localizedMessage = Translator.toLocale(defaultMessage);

      // Add the localized message to errors map
      errors.put(field, localizedMessage);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorMessageResponseDTO(HttpStatus.BAD_REQUEST.value(),
            Translator.toLocale("INVALID_DATA"), errors, request.getServletPath()));
  }

  @ExceptionHandler(InvalidExceptionCustomize.class)
  public ResponseEntity<ErrorMessageResponseDTO> handleInvalidException(
      InvalidExceptionCustomize ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorMessageResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getErrors(),
            request.getServletPath()));
  }

  @ExceptionHandler({DisabledException.class, LockedException.class, AccessDeniedException.class})
  public ResponseEntity<ErrorMessageResponseDTO> handleForbiddenException(Exception ex,
      HttpServletRequest request) {
    String errorMessage;

    if (ex instanceof DisabledException) {
      errorMessage = Translator.toLocale("DISABLE_USER");
    } else if (ex instanceof LockedException) {
      errorMessage = Translator.toLocale("LOCKED_USER");
    } else {
      errorMessage = Translator.toLocale("NOT_ENOUGH_PERMISSION");
    }

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new ErrorMessageResponseDTO(HttpStatus.FORBIDDEN.value(),
            Translator.toLocale("ACCESS_DENIED"), errorMessage, request.getServletPath()));
  }

  @ExceptionHandler({UnauthorizedExceptionCustomize.class, UsernameNotFoundException.class})
  public ResponseEntity<ErrorMessageResponseDTO> handleUnauthorizedException(
      HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
        new ErrorMessageResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            Translator.toLocale("UNAUTHORIZED"), Translator.toLocale("INCORRECT_DATA_LOGIN"),
            request.getServletPath()));
  }

}
