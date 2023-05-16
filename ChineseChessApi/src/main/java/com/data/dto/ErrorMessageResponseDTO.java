package com.data.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorMessageResponseDTO {
    private String message;
    private Map<String, Object> errors;
}
