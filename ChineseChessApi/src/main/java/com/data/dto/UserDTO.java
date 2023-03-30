package com.data.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {    
    private long id;
    private LocalDateTime createdAt;
    private UserProfileDTO userProfileDTO;
    private String roleName;
    private String status;
}
