package com.data.dto.move;

import javax.validation.Valid;

import com.data.dto.PlayBoardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BestMoveRequestDTO {

    @Valid
    private PlayBoardDTO playBoardDTO;

    private Boolean isRed; 

    private int depth;

}
