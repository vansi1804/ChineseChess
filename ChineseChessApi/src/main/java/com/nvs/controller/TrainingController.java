package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.training.TrainingDTO;
import com.nvs.data.dto.training.TrainingDetailDTO;
import com.nvs.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Operation(summary = "Get all base", description = "Endpoint to get all base trainings")
  @GetMapping(value = "")
  public ResponseEntity<List<TrainingDTO>> findAllBase() {
    return ResponseEntity.ok(trainingService.findAllBase());
  }

  @Operation(summary = "Find by id", description = "Endpoint to find an existing training by id")
  @GetMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findById(id));
  }

  @Operation(summary = "Find details by id", description = "Endpoint to find an existing training's details by id")
  @GetMapping(value = "/{id}/details")
  public ResponseEntity<TrainingDetailDTO> findDetailsById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findDetailById(id));
  }

  @Operation(summary = "Create", description = "Endpoint to create new training")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PostMapping(value = "")
  public ResponseEntity<TrainingDTO> create(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Training data to create", required = true, content = @Content(schema = @Schema(implementation = TrainingDTO.class))) @RequestBody @Valid TrainingDTO trainingDTO) {
    return ResponseEntity.ok(trainingService.create(trainingDTO));
  }

  @Operation(summary = "Update", description = "Endpoint to update an existing training")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PutMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> update(@PathVariable long id,
      @RequestBody @Valid TrainingDTO trainingDTO) {
    return ResponseEntity.ok(trainingService.update(id, trainingDTO));
  }

  @Operation(summary = "Delete", description = "Endpoint to delete an existing training. It also deletes all its children or move history")
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.deleteById(id));
  }

}
