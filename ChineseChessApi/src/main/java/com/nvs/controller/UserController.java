package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.common.Default;
import com.nvs.data.dto.user.UserChangePasswordRequestDTO;
import com.nvs.data.dto.user.UserDTO;
import com.nvs.data.dto.user.UserProfileDTO;
import com.nvs.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.USER)
@Tag(name = "User", description = "Endpoints for managing users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Get all users",
      description = "Retrieve a paginated list of all users.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of all users retrieved successfully"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(defaultValue = Default.Page.NO) int no,
      @RequestParam(defaultValue = Default.Page.LIMIT) int limit,
      @RequestParam(defaultValue = Default.Page.SORT_BY) String sortBy) {
    return ResponseEntity.ok(userService.findAll(no, limit, sortBy));
  }

  @Operation(summary = "Change user password",
      description = "Change the password for a user by ID.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/{id}/change-password")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Password changed successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<UserProfileDTO> changePassword(@PathVariable long id,
      @RequestBody @Valid UserChangePasswordRequestDTO userChangePasswordRequestDTO) {
    return ResponseEntity.ok(userService.changePassword(id, userChangePasswordRequestDTO));
  }

  @Operation(summary = "Lock user",
      description = "Lock an existing user by ID to prevent login.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}/lock")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User locked successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<UserDTO> lockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.lockById(id));
  }

  @Operation(summary = "Unlock user",
      description = "Unlock an existing user by ID to allow login.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}/active")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User unlocked successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<UserDTO> unlockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.unlockById(id));
  }

}
