package com.nvs.data.dto.move;

import com.nvs.common.ErrorMessage;
import com.nvs.data.dto.PlayBoardDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MoveCreationDTO extends MoveDetailCreationDTO{

   @NotNull(message = ErrorMessage.NULL_DATA)
   @Valid
   private PlayBoardDTO playBoardDTO;

}
