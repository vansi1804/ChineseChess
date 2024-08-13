package com.nvs.data.dto.user;

import com.nvs.common.ErrorMessage;
import com.nvs.data.dto.VipDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserProfileDTO {

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String phoneNumber;

  @NotBlank(message = ErrorMessage.BLANK_DATA)
  private String name;

  private String avatar;

  private VipDTO vipDTO;

}
