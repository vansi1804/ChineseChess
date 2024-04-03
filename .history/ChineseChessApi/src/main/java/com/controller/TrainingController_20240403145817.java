package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.training.TrainingDTO;
import com.data.dto.training.TrainingDetailDTO;
import com.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.TRAININGS)
@Tag(name = "Training", description = "Endpoints for managing trainings")
@RequiredArgsConstructor
public class TrainingController {

  private final TrainingService trainingService;

  @Operation(
    summary = "Get all base",
    description = "Endpoint to get all base trainings"
  )
  @GetMapping(value = "")
  public ResponseEntity<List<TrainingDTO>> findAllBase() {
    return ResponseEntity.ok(trainingService.findAllBase());
  }

  @Operation(
    summary = "Find by id",
    description = "Endpoint to find an existing training by id"
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<TrainingDTO> findById(
    @Parameter(
      name = "id",
      description = "Id of training",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id
  ) {
    return ResponseEntity.ok(trainingService.findById(id));
  }

  @Operation(
    summary = "Find details by id",
    description = "Endpoint to find an existing training's details by id",
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
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/{id}/details")
  public ResponseEntity<TrainingDetailDTO> findDetailsById(
    @Parameter(
      name = "id",
      description = "Id of training",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id
  ) {
    return ResponseEntity.ok(trainingService.findDetailById(id));
  }

  @Operation(
    summary = "Create",
    description = "Endpoint to create new training",
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
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Conflict",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Training data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = TrainingDTO.class))
    ) @RequestBody @Valid TrainingDTO trainingDTO
  ) {
    return ResponseEntity.ok(trainingService.create(trainingDTO));
  }

  @Operation(
    summary = "Update",
    description = "Endpoint to update an existing training",
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
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "409",
        description = "Conflict",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
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
    @Parameter(
      name = "id",
      description = "Id of training",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id,
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Training data to update",
      required = true,
      content = @Content(schema = @Schema(implementation = TrainingDTO.class))
    ) @RequestBody @Valid TrainingDTO trainingDTO
  ) {
    return ResponseEntity.ok(trainingService.update(id, trainingDTO));
  }

  @Operation(
    summary = "Delete",
    description = "Endpoint to delete an existing training. It also deletes all its children or move history",
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
      @ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "403",
        description = "Forbidden",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Boolean> deleteById(
    @Parameter(
      name = "id",
      description = "Id of training",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id
  ) {
    return ResponseEntity.ok(trainingService.deleteById(id));
  }
}
