package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
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
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Move", description = "Endpoints for managing moving")
@RequiredArgsConstructor
public class MoveController {

  private final MoveService moveService;

  @Operation(
    summary = "Find all available moves",
    description = "Retrieve a list of all available moves"
  )
  @GetMapping(value = "/available")
  public ResponseEntity<List<int[]>> findAllAvailable(
    @RequestBody @Valid AvailableMoveRequestDTO availableMoveRequestDTO
  ) {
    return ResponseEntity.ok(
      moveService.findAllAvailable(availableMoveRequestDTO)
    );
  }

  @Operation(
    summary = "Create a move",
    description = "Endpoint to create a new move"
  )
  @PostMapping(value = "")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid MoveCreationDTO moveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(moveCreationDTO));
  }

  @Operation(
    summary = "Create a move for a match",
    description = "Endpoint to create a move for a match"
  )
  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/match")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid MatchMoveCreationDTO matchMoveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(matchMoveCreationDTO));
  }

  @Operation(
    summary = "Create a move for training",
    description = "Endpoint to create a move for a training"
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @PostMapping(value = "/training")
  public ResponseEntity<MoveDTO> create(
    @RequestBody @Valid TrainingMoveCreationDTO trainingMoveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(trainingMoveCreationDTO));
  }

  @Operation(
    summary = "Find all best available moves",
    description = "Endpoint to retrieve the best available moves"
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