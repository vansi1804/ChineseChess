package com.data.dto.move.availableMove;

import com.common.ErrorMessage;
import com.config.dtoValidation.Validator;
import com.config.dtoValidation.impl.PlayBoardDTOValidatorIml;
import com.data.dto.PlayBoardDTO;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvailableMoveRequestDTO {

  @NotNull(message = ErrorMessage.BLANK_DATA)
  private Integer movingPieceId;

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  @Validator(PlayBoardDTOValidatorIml.class)
  private PlayBoardDTO playBoardDTO;
}
