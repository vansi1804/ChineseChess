package com.data.dto;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private String name;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Boolean color;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private String image;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer currentCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer currentRow;

}
