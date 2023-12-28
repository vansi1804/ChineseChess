package com.controller;

import com.common.ApiUrl;
import com.data.dto.auth.LoginDTO;
import com.data.dto.auth.LoginResponseDTO;
import com.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    description = "Login",
    responses = {
      @ApiResponse(
        content = @Content(
          array = @ArraySchema(
            schema = @Schema(implementation = LoginResponseDTO.class)
          )
        ),
        responseCode = "200"
      ),
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
    }
  )
  @PostMapping(value = "/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
    return ResponseEntity.ok(authService.login(loginDTO));
  }
}
