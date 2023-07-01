package com.data.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TrainingDTO {
    
    private Long id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String title;

    private Long parentTrainingId;

    private List<TrainingDTO> childTrainingDTOs;

}
