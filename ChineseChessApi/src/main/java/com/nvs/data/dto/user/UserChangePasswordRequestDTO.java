package com.nvs.data.dto.user;

import com.nvs.common.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserChangePasswordRequestDTO implements Serializable {

  @NotBlank(message = "NOT_BLANK")
  private String oldPassword;

  @NotBlank(message = "NOT_BLANK")
  @Min(value = Validation.PASSWORD_SIZE_MIN, message = "INVALID_PASSWORD_SIZE")
  private String newPassword;

  private String newPasswordConfirm;

}
