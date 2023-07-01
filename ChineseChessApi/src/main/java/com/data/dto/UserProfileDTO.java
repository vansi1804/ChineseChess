package com.data.dto;

import javax.validation.constraints.NotBlank;

import com.common.ErrorMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {  
    
    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.BLANK_DATA)
    private String name;

    private String avatar;

    private String vipName;

}
