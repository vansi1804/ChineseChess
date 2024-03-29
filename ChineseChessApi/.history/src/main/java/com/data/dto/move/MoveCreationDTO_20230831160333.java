package com.data.dto.move;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MoveCreationDTO extends MoveDetailCreationDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private PlayBoardDTO playBoardDTO;

}
