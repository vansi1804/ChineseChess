package com.nvs.data.dto.player;

import com.nvs.data.dto.user.UserCreationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerCreationDTO {

  @NotNull(message = "NULL_DATA")
  @Valid
  private UserCreationDTO userCreationDTO;

}
