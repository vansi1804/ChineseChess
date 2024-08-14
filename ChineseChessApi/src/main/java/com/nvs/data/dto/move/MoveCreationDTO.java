package com.nvs.data.dto.move;

import com.nvs.config.validation.Validator;
import com.nvs.config.validation.impl.PlayBoardDTOValidatorIml;
import com.nvs.data.dto.PlayBoardDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MoveCreationDTO extends MoveDetailCreationDTO {

  @NotNull(message = "NULL_DATA")
  @Validator(value = {PlayBoardDTOValidatorIml.class})
  private PlayBoardDTO playBoardDTO;

}
