package com.nvs.data.dto.user;

import com.nvs.common.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserCreationDTO extends UserProfileDTO {

  @NotBlank(message = "BLANK_DATA")
  @Min(value = Validation.PASSWORD_SIZE_MIN, message = "INVALID_PASSWORD_SIZE")
  private String password;

}
