package com.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerCreationDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private UserCreationDTO userCreationDTO;
    
}
