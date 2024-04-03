package com.controller;

import com.common.ApiUrl;
import com.data.dto.user.UserChangePasswordRequestDTO;
import com.data.dto.user.UserDTO;
import com.data.dto.user.UserProfileDTO;
import com.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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

  @Operation(
    summary = "Get all",
    description = "Endpoint to retrieve all players"
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<Page<UserDTO>> findAll(
    @RequestParam(defaultValue = Default.Page.NO) int no,
    @RequestParam(defaultValue = Default.Page.LIMIT) int limit,
    @RequestParam(defaultValue = Default.Page.SORT_BY) String sortBy
  ) {
    return ResponseEntity.ok(userService.findAll(no, limit, sortBy));
  }

  @Operation(
    summary = "User change password",
    description = "Endpoint to change password by id"
  )
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/{id}/change-password")
  public ResponseEntity<UserProfileDTO> changePassword(
    @PathVariable long id,
    @RequestBody @Valid UserChangePasswordRequestDTO userChangePasswordRequestDTO
  ) {
    return ResponseEntity.ok(
      userService.changePassword(id, userChangePasswordRequestDTO)
    );
  }

  @Operation(
    summary = "Lock",
    description = "Endpoint to lock an existing user by id"
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}/lock")
  public ResponseEntity<UserDTO> lockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.lockById(id));
  }

  @Operation(
    summary = "Unlock",
    description = "Endpoint to unlock an existing user by id"
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}/active")
  public ResponseEntity<UserDTO> unlockById(@PathVariable long id) {
    return ResponseEntity.ok(userService.unlockById(id));
  }
}
