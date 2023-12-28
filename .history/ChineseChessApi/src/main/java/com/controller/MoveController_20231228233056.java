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
    description = "Retrieve a list of all available moves.",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(type = "array", implementation = int[].class)
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
        responseCode = "500",
        description = "Internal Server Error.",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/available")
  public ResponseEntity<List<int[]>> findAllAvailable(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Available Move Request data",
      required = true,
      content = @Content(schema = @Schema(implementation = AvailableMoveRequestDTO.class))
    ) @RequestBody @Valid AvailableMoveRequestDTO availableMoveRequestDTO
  ) {
    return ResponseEntity.ok(
      moveService.findAllAvailable(availableMoveRequestDTO)
    );
  }

  @Operation(
    summary = "Create a move",
    description = "Endpoint to create a new move",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MoveDTO.class)
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
  @PostMapping(value = "")
  public ResponseEntity<MoveDTO> create(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Move data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = MoveCreationDTO.class))
    ) @RequestBody @Valid MoveCreationDTO moveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(moveCreationDTO));
  }

  @Operation(
    summary = "Create a move for a match",
    description = "Endpoint to create a move for a match",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MoveDTO.class)
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
  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/match")
  public ResponseEntity<MoveDTO> create(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Match move data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = MatchMoveCreationDTO.class))
    ) @RequestBody @Valid MatchMoveCreationDTO matchMoveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(matchMoveCreationDTO));
  }

  @Operation(
    summary = "Create a move for training",
    description = "Endpoint to create a move for a training",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = MoveDTO.class)
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
  @PostMapping(value = "/training")
  public ResponseEntity<MoveDTO> create(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Rank data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = RankDTO.class))
    ) @RequestBody @Valid TrainingMoveCreationDTO trainingMoveCreationDTO
  ) {
    return ResponseEntity.ok(moveService.create(trainingMoveCreationDTO));
  }

  @Operation(
    summary = "Find all best available moves",
    description = "Endpoint to retrieve the best available moves",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(
            type = "object",
            implementation = BestAvailableMoveResponseDTO.class
          )
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
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "/best-available")
  public ResponseEntity<Map<Boolean, BestAvailableMoveResponseDTO>> findAllBestAvailable(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Rank data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = RankDTO.class))
    ) @RequestBody @Valid BestAvailableMoveRequestDTO bestAvailableMoveRequestDTO
  ) {
    return ResponseEntity.ok(
      moveService.findAllBestAvailable(bestAvailableMoveRequestDTO)
    );
  }
}
