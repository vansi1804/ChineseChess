package com.data.dto;

import javax.validation.constraints.Size;

import com.common.ErrorMessage;
import com.common.RegexValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserCreationDTO extends UserProfileDTO{
    @Size(min = RegexValidation.PASSWORD_SIZE, message = ErrorMessage.PASSWORD_SIZE)
    private String password;
}
