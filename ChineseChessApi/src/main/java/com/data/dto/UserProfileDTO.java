package com.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.common.ErrorMessage;
import com.common.RegexValidation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {  
    @Pattern(regexp = RegexValidation.PHONE_NUMBER_REGEX)
    private String phoneNumber;
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;
    private String avatar;
    private String vipName;
}
