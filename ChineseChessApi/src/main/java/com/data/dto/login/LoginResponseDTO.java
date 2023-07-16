package com.data.dto.login;

import java.util.List;

import com.data.dto.JwtResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LoginResponseDTO extends JwtResponseDTO{
    
    private long userId;

    private List<String> roleNames;

}
