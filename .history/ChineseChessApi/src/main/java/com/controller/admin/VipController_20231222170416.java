package com.controller.admin;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.VipDTO;
import com.service.VipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
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
@Tag(
  name = "Vip",
  description = "Endpoints for managing Vips - requires 'ADMIN' role"
)
@RequiredArgsConstructor
public class VipController {

  private final VipService vipService;

 @Operation(
    summary = "Get all vips",
     description = "Endpoint to retrieve all vips",
        responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = VipDTO.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Resource Not Found",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Conflict",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "")
  public ResponseEntity<List<VipDTO>> findAll() {
    return ResponseEntity.ok(vipService.findAll());
  }

  @Operation(
    summary = "Find a vip by ID",
    description = "Endpoint to retrieve a vip by its ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = VipDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Not found"),
    }
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<VipDTO> findById(@PathVariable int id) {
    return ResponseEntity.ok(vipService.findById(id));
  }

  @Operation(
    summary = "Create a vip",
    description = "Endpoint to create a new vip",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Data for creating a vip",
      required = true,
      content = @Content(schema = @Schema(implementation = VipDTO.class))
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = VipDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "409", description = "Conflict"),
    }
  )
  @PostMapping(value = "")
  public ResponseEntity<VipDTO> create(@RequestBody @Valid VipDTO vipDTO) {
    return ResponseEntity.ok(vipService.create(vipDTO));
  }

  @Operation(
    summary = "Update a vip",
    description = "Endpoint to update an existing vip",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Data for updating a vip",
      required = true,
      content = @Content(schema = @Schema(implementation = VipDTO.class))
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = VipDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "409", description = "Conflict"),
    }
  )
  @PutMapping(value = "/{id}")
  public ResponseEntity<VipDTO> update(
    @PathVariable int id,
    @RequestBody @Valid VipDTO vipDTO
  ) {
    return ResponseEntity.ok(vipService.update(id, vipDTO));
  }
}
