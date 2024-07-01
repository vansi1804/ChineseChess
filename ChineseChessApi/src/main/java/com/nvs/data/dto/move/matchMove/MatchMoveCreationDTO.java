package com.nvs.data.dto.move.matchMove;

import com.nvs.common.ErrorMessage;
import com.nvs.data.dto.move.MoveDetailCreationDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MatchMoveCreationDTO extends MoveDetailCreationDTO {

  @NotNull(message = ErrorMessage.NULL_DATA)
  private Long matchId;

  @NotNull(message = ErrorMessage.NULL_DATA)
  private Long playerId;

}
