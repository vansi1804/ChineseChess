package com.nvs.data.dto.match;

import com.nvs.data.dto.match.MatchOthersInfoDTO;
import com.nvs.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
