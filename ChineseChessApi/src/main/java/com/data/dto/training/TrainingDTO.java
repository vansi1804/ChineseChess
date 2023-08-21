package com.data.dto.training;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;
import com.data.dto.AuditorDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TrainingDTO extends AuditorDTO {
    
    private Long id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String title;

    private Long parentTrainingId;

    private List<TrainingDTO> childTrainingDTOs;

}
