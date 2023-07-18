package com.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class VipDTO extends AuditingDTO {

    private Integer id;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Size(min = 1, message = " < 0")
    private Integer depositMilestones;
    
}
