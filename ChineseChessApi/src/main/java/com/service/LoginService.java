package com.service;

import com.data.dto.login.LoginDTO;
import com.data.dto.login.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginDTO loginDTO);
    
}
