package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.MATCHES)
@Tag(name = "Matches", description = "Endpoints for managing matches")
@RequiredArgsConstructor
public class MatchController {

  private MatchService matchService;

  @Operation(
    summary = "Get all matches played by player's id",
    description = "Endpoint to get all matches played by player's id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(type = "array", implementation = MatchDTO.class)
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
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/players/id={playerId}")
  public ResponseEntity<List<MatchDTO>> findAllByPlayerId(
    @Parameter(
      name = "id",
      description = "Id of the player to retrieve all matches",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int32")
    )
    @PathVariable long playerId
  ) {
    return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
  }

  @Operation(
    summary = "Find match by ID",
    description = "Retrieves details of a match by its ID",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MatchDTO.class))
      ),
    }
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<?> findDetailById(@PathVariable long id) {
    return ResponseEntity.ok(matchService.findDetailById(id));
  }

  @Operation(
    summary = "Create a new match",
    description = "Creates a new match",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Match data for creating a new match",
      required = true,
      content = @Content(
        schema = @Schema(implementation = MatchCreationDTO.class)
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MatchDTO.class))
      ),
    }
  )
  @PostMapping(value = "")
  public ResponseEntity<MatchDTO> create(
    @RequestBody @Valid MatchCreationDTO matchCreationDTO
  ) {
    return ResponseEntity.ok(matchService.create(matchCreationDTO));
  }

  @Operation(
    summary = "Update match result",
    description = "Updates the result of a match by its ID",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "New result value for updating the match",
      required = true
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = MatchDTO.class))
      ),
    }
  )
  @PutMapping(value = "/id/{id}/result={result}")
  public ResponseEntity<MatchDTO> updateResult(
    @PathVariable long id,
    @PathVariable int result
  ) {
    return ResponseEntity.ok(matchService.updateResult(id, result));
  }
}
