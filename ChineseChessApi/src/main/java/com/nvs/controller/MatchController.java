package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.data.dto.match.MatchCreationDTO;
import com.nvs.data.dto.match.MatchDTO;
import com.nvs.data.dto.match.MatchDetailDTO;
import com.nvs.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.MATCHES)
@Tag(name = "Matches", description = "Endpoints for managing matches")
@RequiredArgsConstructor
public class MatchController {

  private final MatchService matchService;

  @Operation(summary = "Get all matches by player's ID", description = "Retrieve all matches played by a player's ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Matches successfully retrieved"),
      @ApiResponse(responseCode = "404", description = "Player not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/players/{playerId}")
  public ResponseEntity<List<MatchDTO>> findAllByPlayerId(@PathVariable long playerId) {
    return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
  }

  @Operation(summary = "Find match details by ID", description = "Retrieve match details by match ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Match details successfully retrieved"),
      @ApiResponse(responseCode = "404", description = "Match not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{id}")
  public ResponseEntity<MatchDetailDTO> findDetailById(@PathVariable long id) {
    return ResponseEntity.ok(matchService.findDetailById(id));
  }

  @Operation(summary = "Create a new match", description = "Create a new match played by two players.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Match successfully created"),
      @ApiResponse(responseCode = "400", description = "Invalid match creation request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "")
  public ResponseEntity<MatchDTO> create(@RequestBody @Valid MatchCreationDTO matchCreationDTO) {
    return ResponseEntity.ok(matchService.create(matchCreationDTO));
  }

  @Operation(summary = "Update match result", description = "Update the result of an existing match.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Match result successfully updated"),
      @ApiResponse(responseCode = "404", description = "Match not found"),
      @ApiResponse(responseCode = "400", description = "Invalid result update request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PutMapping(value = "/{id}")
  public ResponseEntity<MatchDTO> updateResult(@PathVariable long id,
      @RequestParam(required = false) Boolean result) {
    return ResponseEntity.ok(matchService.updateResult(id, result));
  }
}
