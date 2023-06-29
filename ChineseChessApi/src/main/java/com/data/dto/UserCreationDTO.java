package com.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class UserCreationDTO extends UserProfileDTO{
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    @Size(min = Validation.PASSWORD_SIZE_MIN, message = ErrorMessage.PASSWORD_TOO_SHORT)
    private String password;
}
