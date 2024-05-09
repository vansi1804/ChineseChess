package com.nvs.data.dto;

import com.nvs.data.dto.PieceDTO;
import com.nvs.common.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayBoardDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private PieceDTO[][] state;
}
