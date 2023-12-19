package com.data.dto.move.availableMove.bestAvailableMove;

import com.common.ErrorMessage;
import com.data.dto.PlayBoardDTO;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
