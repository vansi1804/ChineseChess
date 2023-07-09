package com.data.dto;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MatchMoveCreationDTO extends MoveDTO{
    
    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long matchId;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long playerId;

}
