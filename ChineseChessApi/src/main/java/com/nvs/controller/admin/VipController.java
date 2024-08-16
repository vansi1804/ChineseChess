package com.nvs.controller.admin;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.VipDTO;
import com.nvs.service.VipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.VIPS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Vip", description = "Endpoints for managing Vips - requires 'ADMIN' role")
public class VipController {

  private final VipService vipService;

  @Operation(summary = "Get all vips", description = "Retrieve a list of all vips.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved all vips"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "")
  public ResponseEntity<List<VipDTO>> findAll() {
    return ResponseEntity.ok(vipService.findAll());
  }

  @Operation(summary = "Find a vip by ID", description = "Retrieve a vip by its ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved the vip"),
      @ApiResponse(responseCode = "404", description = "Vip not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "/{id}")
  public ResponseEntity<VipDTO> findById(@PathVariable int id) {
    return ResponseEntity.ok(vipService.findById(id));
  }

  @Operation(summary = "Create a new vip", description = "Create a new vip with the provided details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successfully created the vip"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "")
  public ResponseEntity<VipDTO> create(@RequestBody @Valid VipDTO vipDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(vipService.create(vipDTO));
  }

  @Operation(summary = "Update an existing vip", description = "Update an existing vip with the provided details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully updated the vip"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "404", description = "Vip not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping(value = "/{id}")
  public ResponseEntity<VipDTO> update(@PathVariable int id, @RequestBody @Valid VipDTO vipDTO) {
    return ResponseEntity.ok(vipService.update(id, vipDTO));
  }
}
