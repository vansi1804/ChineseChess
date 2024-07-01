package com.nvs.data.dto.move.availableMove.bestAvailableMove;

import com.nvs.common.ErrorMessage;
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

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  private PlayBoardDTO playBoardDTO;

  private Boolean isRed;

  @NotNull(message = ErrorMessage.NULL_DATA)
  private Integer depth;

}
