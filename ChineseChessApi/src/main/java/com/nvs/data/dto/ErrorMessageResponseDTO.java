package com.nvs.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ErrorMessageResponseDTO implements Serializable {
  private int code;

  private String message;

  private Object errors;

  private String path;
}
