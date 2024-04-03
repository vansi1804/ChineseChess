package com.controller;

import com.common.ApiUrl;
import com.data.dto.move.MoveCreationDTO;
import com.data.dto.move.MoveDTO;
import com.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.service.MoveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.MOVES)
@Tag(name = "Move", description = "Endpoints for managing matches")
@RequiredArgsConstructor
public class MoveController {

  private final MoveService moveService;

  @Operation(
    summary = "Find all available moves",
    description = "Retrieves all available moves based on the given request",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Request body for finding available moves",
      required = true,
      content = @Content(
        schema = @Schema(implementation = AvailableMoveRequestDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = List.class))
      ),
    }
  )
  @PostMapping(value = "/available")
  public ResponseEntity<List<int[]>> findAllAvailable(
    @RequestBody @Valid AvailableMoveRequestDTO availableMoveRequestDTO
  ) {
    return ResponseEntity.ok(
      moveService.findAllAvailable(availableMoveRequestDTO)
    );
  }

  @Operation(
    summary = "Create a new move",
    description = "Create a new move based on the provided details",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Request body for creating a new move",
      required = true,
      content = @Content(
        schema = @Schema(implementation = MoveCreationDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MoveDTO.class))
      ),
    }
  )
  @PostMapping(value = "")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid MoveCreationDTO moveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(moveCreationDTO));
  }

  @Operation(
    summary = "Create a move for a match",
    description = "Create a move for a match based on the provided details",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Request body for creating a move for a match",
      required = true,
      content = @Content(
        schema = @Schema(implementation = MatchMoveCreationDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MoveDTO.class))
      ),
    }
  )
  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/match")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid MatchMoveCreationDTO moveHistoryCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(moveHistoryCreationDTO));
  }

  @Operation(
    summary = "Create a move for training",
    description = "Create a move for training based on the provided details",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Request body for creating a move for training",
      required = true,
      content = @Content(
        schema = @Schema(implementation = TrainingMoveCreationDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MoveDTO.class))
      ),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PostMapping(value = "/training")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid TrainingMoveCreationDTO trainingMoveHistoryCreationDTO
  ) {
    return ResponseEntity.ok(
      moveService.create(trainingMoveHistoryCreationDTO)
    );
  }

  @Operation(
    summary = "Find all best available moves",
    description = "Retrieves all best available moves based on the given request",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Request body for finding best available moves",
      required = true,
      content = @Content(
        schema = @Schema(implementation = BestAvailableMoveRequestDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = Map.class))
      ),
    }
  )
  @GetMapping(value = "/best-available")
  public ResponseEntity<Map<Boolean, BestAvailableMoveResponseDTO>> findAllBestAvailable(
    @RequestBody @Valid BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO
  ) {
    return ResponseEntity.ok(
      moveService.findAllBestAvailable(bestAvailableMoveRequestDTO)
    );
  }
}
