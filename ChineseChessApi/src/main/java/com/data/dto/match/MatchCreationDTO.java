package com.data.dto.match;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.config.validation.Validator;
import com.config.validation.impl.MatchOthersInfoCreationValidator;

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
    @Validator(MatchOthersInfoCreationValidator.class)
    @Valid
    private MatchOthersInfoDTO matchOthersInfoDTO;

}
