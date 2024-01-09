package com.data.dto.move.trainingMove;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.data.dto.move.MoveDetailCreationDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TrainingMoveCreationDTO extends MoveDetailCreationDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Long trainingId;

}
