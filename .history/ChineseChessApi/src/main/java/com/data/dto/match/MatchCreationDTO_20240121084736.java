package com.data.dto.match;

import com.common.ErrorMessage;
import com.config.dtoValidation.Validator;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO {

  @NotNull(message = ErrorMessage.NULL_DATA)
  private Long player1Id;

  @NotNull(message = ErrorMessage.NULL_DATA)
  private Long player2Id;

  @NotNull(message = ErrorMessage.NULL_DATA)
  @Valid
  private MatchOthersInfoDTO matchOthersInfoDTO;
}
