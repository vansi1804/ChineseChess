package com.nvs.data.dto;

import com.nvs.common.ErrorMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayBoardDTO {

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  private PieceDTO[][] state;

}
