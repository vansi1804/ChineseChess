package com.dto;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RoleDTO {
    private int id;
    @NotBlank(message = ErrorMessage.BLANK_NAME)
    private String name;
}
