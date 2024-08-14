package com.nvs.controller.admin;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.VipDTO;
import com.nvs.service.VipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  @Operation(summary = "Get all vips", description = "Endpoint to retrieve all vips")
  @GetMapping(value = "")
  public ResponseEntity<List<VipDTO>> findAll() {
    return ResponseEntity.ok(vipService.findAll());
  }

  @Operation(summary = "Find a vip by id", description = "Endpoint to update an existing vip")
  @GetMapping(value = "/{id}")
  public ResponseEntity<VipDTO> findById(@PathVariable int id) {
    return ResponseEntity.ok(vipService.findById(id));
  }

  @Operation(summary = "Create a vip", description = "Endpoint to create a new vip")
  @PostMapping(value = "")
  public ResponseEntity<VipDTO> create(@RequestBody @Valid VipDTO vipDTO) {
    return ResponseEntity.ok(vipService.create(vipDTO));
  }

  @Operation(summary = "Update a vip", description = "Endpoint to update an existing vip")
  @PutMapping(value = "/{id}")
  public ResponseEntity<VipDTO> update(@PathVariable int id, @RequestBody @Valid VipDTO vipDTO) {
    return ResponseEntity.ok(vipService.update(id, vipDTO));
  }

}
