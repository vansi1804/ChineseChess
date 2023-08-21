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
public class VipDTO extends AuditorDTO {

    private Integer id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Min(value = Validation.DEPOSIT_MILESTONES, message = ErrorMessage.DEPOSIT_MILESTONES)
    private Integer depositMilestones;
    
}
