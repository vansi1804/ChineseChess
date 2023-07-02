package com.data.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.common.Default;
import com.common.ErrorMessage;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date createdDate;

}
