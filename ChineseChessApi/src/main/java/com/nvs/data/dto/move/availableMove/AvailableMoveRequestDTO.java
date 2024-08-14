package com.nvs.data.dto.move.availableMove;

import com.nvs.config.validation.Validator;
import com.nvs.config.validation.impl.PlayBoardDTOValidatorIml;
import com.nvs.data.dto.PlayBoardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvailableMoveRequestDTO {

  @NotNull(message = "NULL_DATA")
  private Integer movingPieceId;

  @NotNull(message = "NULL_DATA")
  @Validator(PlayBoardDTOValidatorIml.class)
  private PlayBoardDTO playBoardDTO;

}
