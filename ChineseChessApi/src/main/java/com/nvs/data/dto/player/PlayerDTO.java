package com.nvs.data.dto.player;

import com.nvs.data.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerDTO{

   private long id;

   private UserDTO userDTO;

   private PlayerOthersInfoDTO playerOthersInfoDTO;

}
