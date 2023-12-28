package com.controller;

import com.common.ApiUrl;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.USER)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@Tag(name = "User", description = "Endpoints for managing users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(
    summary = "Lock user by ID",
    description = "Lock a user by their ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PutMapping(value = "/id/{id}/lock")
  public ResponseEntity<Boolean> lockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.lockById(id));
  }

  @Operation(
    summary = "Unlock user by ID",
    description = "Unlock a user by their ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PutMapping(value = "/id/{id}/active")
  public ResponseEntity<Boolean> unlockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.unlockById(id));
  }
}
