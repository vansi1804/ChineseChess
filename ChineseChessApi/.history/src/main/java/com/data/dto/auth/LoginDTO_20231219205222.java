package com.data.dto.auth;

import com.common.ErrorMessage;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO implements Serializable {

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String phoneNumber;

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String password;
}
