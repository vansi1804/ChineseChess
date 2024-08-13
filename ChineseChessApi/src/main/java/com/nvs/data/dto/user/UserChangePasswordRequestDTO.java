package com.nvs.data.dto.user;

import com.nvs.common.ErrorMessage;
import com.nvs.common.Validation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserChangePasswordRequestDTO implements Serializable {

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String oldPassword;

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  @Size(min = Validation.PASSWORD_SIZE_MIN, message = ErrorMessage.INVALID_PASSWORD_SIZE)
  private String newPassword;

  private String newPasswordConfirm;

}
