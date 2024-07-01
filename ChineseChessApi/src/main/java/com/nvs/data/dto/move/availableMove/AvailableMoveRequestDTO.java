package com.nvs.data.dto.move.availableMove;

import com.nvs.common.ErrorMessage;
import com.nvs.config.dtoValidation.Validator;
import com.nvs.config.dtoValidation.impl.PlayBoardDTOValidatorIml;
import com.nvs.data.dto.PlayBoardDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AvailableMoveRequestDTO{

   @NotNull(message = ErrorMessage.BLANK_DATA)
   private Integer movingPieceId;

   @NotNull(message = ErrorMessage.NULL_DATA)
   @Valid
   @Validator(PlayBoardDTOValidatorIml.class)
   private PlayBoardDTO playBoardDTO;

}
