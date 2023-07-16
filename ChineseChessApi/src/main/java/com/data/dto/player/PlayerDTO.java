package com.data.dto.player;

import com.data.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
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
