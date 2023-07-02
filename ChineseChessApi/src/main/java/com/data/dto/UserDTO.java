package com.data.dto;

import java.util.Date;

import com.common.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {    
    
    private long id;

    private UserProfileDTO userProfileDTO;

    private String roleName;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Default.DateTimeFormat.DATE_TIME)
    private Date createdDate;
    
}
