package com.data.dto;

import java.util.Date;

import com.common.RegexValidation;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {    
    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = RegexValidation.DATE_TIME)
    private Date createdDate;
    private UserProfileDTO userProfileDTO;
    private String roleName;
    private String status;
}
