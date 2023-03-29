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
public class LevelsDTO{
    private int id;
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;
    private int milestones;
    List<PlayerDTO> playerDTOs;
}
