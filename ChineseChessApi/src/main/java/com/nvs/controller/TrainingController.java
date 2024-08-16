package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.training.TrainingDTO;
import com.nvs.data.dto.training.TrainingDetailDTO;
import com.nvs.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.TRAININGS)
@Tag(name = "Training", description = "Endpoints for managing trainings")
@RequiredArgsConstructor
public class TrainingController {

  private final TrainingService trainingService;

  @Operation(summary = "Get all base trainings",
      description = "Retrieve a list of all base trainings.")
  @GetMapping(value = "")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of all base trainings"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<List<TrainingDTO>> findAllBase() {
    return ResponseEntity.ok(trainingService.findAllBase());
  }

  @Operation(summary = "Find training by id",
      description = "Retrieve training details by its ID.")
  @GetMapping(value = "/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training found by ID"),
      @ApiResponse(responseCode = "404", description = "Training not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<TrainingDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findById(id));
  }

  @Operation(summary = "Find training details by id",
      description = "Retrieve detailed information of a training by its ID.")
  @GetMapping(value = "/{id}/details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training details found by ID"),
      @ApiResponse(responseCode = "404", description = "Training not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<TrainingDetailDTO> findDetailsById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findDetailById(id));
  }

  @Operation(summary = "Create new training",
      description = "Create a new training entry. Requires 'ADMIN' role.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping(value = "")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<TrainingDTO> create(
      @RequestBody @Valid TrainingDTO trainingDTO) {
    return ResponseEntity.ok(trainingService.create(trainingDTO));
  }

  @Operation(summary = "Update training",
      description = "Update an existing training entry. Requires 'ADMIN' role.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize("hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training updated successfully"),
      @ApiResponse(responseCode = "404", description = "Training not found"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<TrainingDTO> update(@PathVariable long id,
      @RequestBody @Valid TrainingDTO trainingDTO) {
    return ResponseEntity.ok(trainingService.update(id, trainingDTO));
  }

  @Operation(summary = "Delete training",
      description = "Delete an existing training by ID. Also deletes related children or move history. Requires 'ADMIN' role.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @PreAuthorize("hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Training not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.deleteById(id));
  }

}
