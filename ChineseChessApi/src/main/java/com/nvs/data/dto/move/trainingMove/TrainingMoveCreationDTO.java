package com.nvs.data.dto.move.trainingMove;

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

public class TrainingMoveCreationDTO extends MoveDetailCreationDTO {

  @NotNull(message = "NULL_DATA")
  private Long trainingId;

}
