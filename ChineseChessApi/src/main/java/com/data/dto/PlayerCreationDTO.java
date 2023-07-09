package com.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.common.ErrorMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCreationDTO {

    @NotNull(message = ErrorMessage.NULL_DATA)
    @Valid
    private UserCreationDTO userCreationDTO;
    
}
