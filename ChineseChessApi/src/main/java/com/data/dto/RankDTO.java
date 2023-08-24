package com.data.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;
import com.common.Validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RankDTO extends AuditorDTO {

    private Integer id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.ELO_MILESTONES, message = ErrorMessage.ELO_MILESTONES)
    private Integer eloMilestones;

}
