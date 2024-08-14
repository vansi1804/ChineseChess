package com.nvs.data.dto.match;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MatchCreationDTO {

  @NotBlank(message = "BLANK_DATA")
  private Long player1Id;

  @NotBlank(message = "BLANK_DATA")
  private Long player2Id;

  @NotNull(message = "NULL_DATA")
  @Valid
  private MatchOthersInfoDTO matchOthersInfoDTO;

}
