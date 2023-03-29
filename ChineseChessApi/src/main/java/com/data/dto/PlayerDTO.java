package com.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerDTO {    // show for admin
    private long id;
    private UserDTO userDTO;
    private String levelsName;
    private int elo;
}
