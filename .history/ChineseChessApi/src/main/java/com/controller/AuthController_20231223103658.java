package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.auth.LoginDTO;
import com.data.dto.auth.LoginResponseDTO;
import com.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

  @Operation(
    summary = "Login",
    description = "Endpoint to login",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = LoginResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @PostMapping(value = "/login")
  public ResponseEntity<?> login(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Login data",
      required = true,
      content = @Content(schema = @Schema(implementation = LoginDTO.class))
    ) @RequestBody @Valid LoginDTO loginDTO
  ) {
    return ResponseEntity.ok(authService.login(loginDTO));
  }
}
