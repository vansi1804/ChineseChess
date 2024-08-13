package com.nvs.data.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ErrorMessageResponseDTO implements Serializable {

  private int code;

  private String message;

  private Object errors;

  private String path;

}
