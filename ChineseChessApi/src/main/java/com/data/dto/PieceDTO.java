package com.data.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PieceDTO implements Serializable{

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer id;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private String name;

    private boolean isRed;

    private String image;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer currentCol;

    @NotNull(message = ErrorMessage.NULL_DATA)
    private Integer currentRow;

}
