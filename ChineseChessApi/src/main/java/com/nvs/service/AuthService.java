package com.nvs.service;

import com.nvs.data.dto.auth.LoginRequestDTO;
import com.nvs.data.dto.auth.LoginResponseDTO;

public interface AuthService {

  LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

}
