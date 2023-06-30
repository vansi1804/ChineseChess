package com.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingMatchDTO {
    private long id;
    private TrainingDTO trainingDTO;
    private PlayBoardDTO playBoardStartDTO;
}
