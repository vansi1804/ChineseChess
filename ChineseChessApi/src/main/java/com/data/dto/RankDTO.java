package com.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RankDTO{
    
    private int id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    @Size(min = 1, message = "eloMilestones > 0")
    private Integer eloMilestones;

}
