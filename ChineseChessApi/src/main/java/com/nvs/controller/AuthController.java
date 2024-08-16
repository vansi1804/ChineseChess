package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.auth.LoginRequestDTO;
import com.nvs.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.AUTH)
@Tag(name = "Auth", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Login", description = "Authenticate a user and return an authentication token.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully authenticated, returns an authentication token"),
      @ApiResponse(responseCode = "400", description = "Invalid login credentials or request format"),
      @ApiResponse(responseCode = "401", description = "Authentication failed"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
    return ResponseEntity.ok(authService.login(loginRequestDTO));
  }
}
