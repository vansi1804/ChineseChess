package com.controller;

import com.common.ApiUrl;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.USER)
@PreAuthorize(value = "hasAuthority('ADMIN')")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

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
