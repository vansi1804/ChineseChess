package com.nvs.data.dto.user;

import com.nvs.common.ErrorMessage;
import com.nvs.common.Validation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserCreationDTO extends UserProfileDTO{

   @NotBlank(message = ErrorMessage.BLANK_DATA)
   @Size(min = Validation.PASSWORD_SIZE_MIN, message = ErrorMessage.INVALID_PASSWORD_SIZE)
   private String password;

}
