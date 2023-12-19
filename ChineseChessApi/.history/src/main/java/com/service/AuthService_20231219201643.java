package com.service;

import com.data.dto.auth.LoginDTO;
import com.data.dto.auth.LoginResponseDTO;

public interface AuthService {
  LoginResponseDTO login(LoginDTO loginDTO);
}
