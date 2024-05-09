package com.nvs.service;

import com.nvs.data.dto.auth.LoginDTO;
import com.nvs.data.dto.auth.LoginResponseDTO;

public interface AuthService {
  LoginResponseDTO login(LoginDTO loginDTO);
}
