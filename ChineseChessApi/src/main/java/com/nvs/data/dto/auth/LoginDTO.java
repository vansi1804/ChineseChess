package com.nvs.data.dto.auth;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO implements Serializable {

  @NotBlank(message = "BLANK_DATA")
  private String phoneNumber;

  @NotBlank(message = "BLANK_DATA")
  private String password;

}
