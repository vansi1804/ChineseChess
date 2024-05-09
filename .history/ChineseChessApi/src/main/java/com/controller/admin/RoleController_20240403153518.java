package com.controller.admin;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.RoleDTO;
import com.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(
  name = "Role",
  description = "Endpoints for managing Roles - requires 'ADMIN' role"
)
class RoleController {

  private final RoleService roleService;

  @Operation(
    summary = "Get all roles",
    description = "Endpoint to retrieve all roles"
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(roleService.findAll());
  }
}