package com.nvs.data.dto.player;

import com.nvs.common.ErrorMessage;
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

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  private UserProfileDTO userProfileDTO;

  private com.nvs.data.dto.player.PlayerOthersInfoDTO playerOthersInfoDTO;

}
