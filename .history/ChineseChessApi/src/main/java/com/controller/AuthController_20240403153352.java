package com.controller;

import com.common.ApiUrl;
import com.data.dto.auth.LoginDTO;
import com.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.AUTH)
@Tag(name = "Auth", description = "Endpoints for managing auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Login", description = "Endpoint to login")
  @PostMapping(value = "/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
    return ResponseEntity.ok(authService.login(loginDTO));
  }
}
