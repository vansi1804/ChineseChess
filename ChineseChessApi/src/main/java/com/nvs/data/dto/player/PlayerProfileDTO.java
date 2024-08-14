package com.nvs.data.dto.player;

import com.nvs.data.dto.user.UserProfileDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerProfileDTO {

  @NotNull(message = "NULL_DATA")
  @Valid
  private UserProfileDTO userProfileDTO;

  private PlayerOthersInfoDTO playerOthersInfoDTO;

}
