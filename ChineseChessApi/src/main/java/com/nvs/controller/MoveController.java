package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.move.MoveCreationDTO;
import com.nvs.data.dto.move.MoveDTO;
import com.nvs.data.dto.move.availableMove.AvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveRequestDTO;
import com.nvs.data.dto.move.availableMove.bestAvailableMove.BestAvailableMoveResponseDTO;
import com.nvs.data.dto.move.matchMove.MatchMoveCreationDTO;
import com.nvs.data.dto.move.trainingMove.TrainingMoveCreationDTO;
import com.nvs.service.MoveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.MOVES)
@Tag(name = "Move", description = "Endpoints for managing moves")
@RequiredArgsConstructor
public class MoveController {

  private final MoveService moveService;

  @Operation(summary = "Find all available moves", description = "Retrieve a list of all available moves")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Available moves successfully retrieved"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/available")
  public ResponseEntity<List<int[]>> findAllAvailable(
      @RequestBody @Valid AvailableMoveRequestDTO availableMoveRequestDTO) {
    return ResponseEntity.ok(moveService.findAllAvailable(availableMoveRequestDTO));
  }

  @Operation(summary = "Create a move", description = "Create a new move")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Move successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid move creation request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "")
  public ResponseEntity<MoveDTO> createMove(
      @RequestBody @Valid MoveCreationDTO moveCreationDTO) {
    return ResponseEntity.ok(moveService.create(moveCreationDTO));
  }

  @Operation(summary = "Create a move for a match", description = "Create a move for a match")
  @PreAuthorize("isAuthenticated()")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Match move successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid match move creation request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/match")
  public ResponseEntity<MoveDTO> createMatchMove(
      @RequestBody @Valid MatchMoveCreationDTO matchMoveCreationDTO) {
    return ResponseEntity.ok(moveService.create(matchMoveCreationDTO));
  }

  @Operation(summary = "Create a move for training", description = "Create a move for training purposes")
  @PreAuthorize("hasAuthority('ADMIN')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Training move successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid training move creation request"),
      @ApiResponse(responseCode = "403", description = "Access denied"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/training")
  public ResponseEntity<MoveDTO> createTrainingMove(
      @RequestBody @Valid TrainingMoveCreationDTO trainingMoveCreationDTO) {
    return ResponseEntity.ok(moveService.create(trainingMoveCreationDTO));
  }

  @Operation(summary = "Find all best available moves", description = "Retrieve the best available moves based on the provided criteria")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Best available moves successfully retrieved"),
      @ApiResponse(responseCode = "400", description = "Invalid request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/best-available")
  public ResponseEntity<Map<Boolean, BestAvailableMoveResponseDTO>> findAllBestAvailable(
      @RequestBody @Valid BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO) {
    return ResponseEntity.ok(moveService.findAllBestAvailable(bestAvailableMoveRequestDTO));
  }
}
