package com.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerDTO {

    private long id;

    private UserDTO userDTO;

    private String rankName;

    private int elo;

    private int win;

    private int draw;

    private int lose;

}
