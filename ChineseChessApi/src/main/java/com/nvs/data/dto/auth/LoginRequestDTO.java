package com.nvs.data.dto.auth;

import com.nvs.config.logging.SensitiveData;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginRequestDTO implements Serializable {

  @SensitiveData
  @NotBlank(message = "BLANK_DATA")
  private String phoneNumber;

  @SensitiveData
  @NotBlank(message = "BLANK_DATA")
  private String password;

}
