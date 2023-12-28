package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.TRAININGS)
@Tag(name = "Training", description = "Endpoints for managing trainings")
@RequiredArgsConstructor
public class TrainingController {

  private final TrainingService trainingService;

  @Operation(
    summary = "Get all base trainings",
    description = "Endpoint to get all base trainings",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "")
  public ResponseEntity<List<TrainingDTO>> findAllBase() {
    return ResponseEntity.ok(trainingService.findAllBase());
  }

  @Operation(
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.findById(id));
  }

  @Operation(
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/{id}/details")
  public ResponseEntity<TrainingDetailDTO> findDetailsById(
    @PathVariable long id
  ) {
    return ResponseEntity.ok(trainingService.findDetailById(id));
  }

  @Operation(
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
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
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
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
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = TrainingDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Boolean> deleteById(@PathVariable long id) {
    return ResponseEntity.ok(trainingService.deleteById(id));
  }
}
