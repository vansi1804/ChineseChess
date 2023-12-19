package com.data.dto.player;

import com.common.ErrorMessage;
import com.data.dto.user.UserCreationDTO;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerCreationDTO {

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  private UserCreationDTO userCreationDTO;
}
