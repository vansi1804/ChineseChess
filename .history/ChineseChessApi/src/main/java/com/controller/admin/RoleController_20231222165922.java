package com.controller.admin;

import com.common.ApiUrl;
import com.data.dto.RoleDTO;
import com.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(
  name = "Role",
  description = "Endpoints for managing Roles - requires 'ADMIN' role"
)
@RequiredArgsConstructor
class RoleController {

  private final RoleService roleService;

  @Operation(
    description = "Find all roles",
    responses = {
      @ApiResponse(
        content = @Content(schema = @Schema(implementation = RoleDTO.class)
        ),
        responseCode = "200"
      ),
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<?> findAll() {
    return ResponseEntity.ok(roleService.findAll());
  }
}
