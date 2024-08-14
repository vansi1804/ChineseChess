package com.nvs.data.dto.move.availableMove.bestAvailableMove;

import com.nvs.config.validation.Validator;
import com.nvs.config.validation.impl.PlayBoardDTOValidatorIml;
import com.nvs.data.dto.PlayBoardDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestAvailableMoveRequestDTO {

  @NotNull(message = "NULL_DATA")
  @Valid
  @Validator(value = {PlayBoardDTOValidatorIml.class})
  private PlayBoardDTO playBoardDTO;

  private Boolean isRed;

  @NotNull(message = "NULL_DATA")
  private Integer depth;

}
