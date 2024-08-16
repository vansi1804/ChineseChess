package com.nvs.controller.admin;

import com.nvs.common.ApiUrl;
import com.nvs.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.ROLES)
@RequiredArgsConstructor
@Tag(name = "Role", description = "Endpoints for managing roles - requires 'ADMIN' role")
public class RoleController {

  private final RoleService roleService;

  @Operation(summary = "Get all roles", description = "Retrieve a list of all roles.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all roles"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(roleService.findAll());
  }
}
