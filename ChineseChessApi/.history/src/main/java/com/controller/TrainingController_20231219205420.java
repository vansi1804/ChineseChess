package com.controller;

import com.common.ApiUrl;
import com.data.dto.training.TrainingDTO;
import com.data.dto.training.TrainingDetailDTO;
import com.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TRAININGS)
@Tag(name = "Training", description = "Endpoints for managing trainings")
@RequiredArgsConstructor
public class TrainingController {

  private final TrainingService trainingService;

  @Operation(
    summary = "Find all trainings",
    description = "Retrieve a list of all trainings",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @GetMapping(value = "")
  public ResponseEntity<List<TrainingDTO>> findAllBase() {
    return ResponseEntity.ok(trainingService.findAllBase());
  }

  @Operation(
    summary = "Find training by ID",
    description = "Retrieve a training by its ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findById(id));
  }

  @Operation(
    summary = "Find training details by ID",
    description = "Retrieve the details of a training by its ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @GetMapping(value = "/id/{id}/details")
  public ResponseEntity<TrainingDetailDTO> findDetailsById(
    @PathVariable long id
  ) {
    return ResponseEntity.ok(trainingService.findDetailById(id));
  }

  @Operation(
    summary = "Create a training",
    description = "Create a new training",
    requestBody = @RequestBody(
      description = "Training data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = TrainingDTO.class))
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PostMapping(value = "")
  public ResponseEntity<TrainingDTO> create(
    @RequestBody @Valid TrainingDTO trainingDTO
  ) {
    return ResponseEntity.ok(trainingService.create(trainingDTO));
  }

  @Operation(
    summary = "Update a training",
    description = "Update an existing training by its ID",
    requestBody = @RequestBody(
      description = "Training data to update",
      required = true,
      content = @Content(schema = @Schema(implementation = TrainingDTO.class))
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> update(
    @PathVariable long id,
    @RequestBody @Valid TrainingDTO trainingDTO
  ) {
    return ResponseEntity.ok(trainingService.update(id, trainingDTO));
  }

  @Operation(
    summary = "Delete a training",
    description = "Delete a training by its ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.deleteById(id));
  }
}
