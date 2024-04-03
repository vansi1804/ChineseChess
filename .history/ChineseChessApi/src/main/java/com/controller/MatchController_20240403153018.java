package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.match.MatchCreationDTO;
import com.data.dto.match.MatchDTO;
import com.data.dto.match.MatchDetailDTO;
import com.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.MATCHES)
@Tag(name = "Matches", description = "Endpoints for managing matches")
@RequiredArgsConstructor
public class MatchController {

  private MatchService matchService;

  @Operation(
    summary = "Get all by player's id",
    description = "Endpoint to get all matches played by player's id"
  )
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/players/{playerId}")
  public ResponseEntity<List<MatchDTO>> findAllByPlayerId(
  @PathVariable long playerId
  ) {
    return ResponseEntity.ok(matchService.findAllByPlayerId(playerId));
  }

  @Operation(
    summary = "Find details by id",
    description = "Endpoint to find match's details by id"
  )
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{id}")
  public ResponseEntity<MatchDetailDTO> findDetailById(
    @PathVariable long id
  ) {
    return ResponseEntity.ok(matchService.findDetailById(id));
  }

  @Operation(
    summary = "Create",
    description = "Endpoint to create a new match played by 2 player"
  )
  @PostMapping(value = "")
  public ResponseEntity<MatchDTO> create(
     @RequestBody @Valid MatchCreationDTO matchCreationDTO
  ) {
    return ResponseEntity.ok(matchService.create(matchCreationDTO));
  }

  @Operation(
    summary = "Update result",
    description = "Endpoint to update an existing match's result"
  )
  @PutMapping(value = "/{id}")
  public ResponseEntity<MatchDTO> updateResult(
    @PathVariable long id,
    @RequestParam Boolean result
  ) {
    return ResponseEntity.ok(matchService.updateResult(id, result));
  }
}
