package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.common.Default;
import com.nvs.data.dto.player.PlayerCreationDTO;
import com.nvs.data.dto.player.PlayerDTO;
import com.nvs.data.dto.player.PlayerProfileDTO;
import com.nvs.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping(ApiUrl.PLAYERS)
@Tag(name = "Player", description = "Endpoints for managing players")
@RequiredArgsConstructor
public class PlayerController {

  private final PlayerService playerService;

  @Operation(summary = "Get all players",
      description = "Retrieve a paginated list of all players. Requires 'ADMIN' role.")
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of all players"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Page<PlayerDTO>> findAll(
      @RequestParam(defaultValue = Default.Page.NO) int no,
      @RequestParam(defaultValue = Default.Page.LIMIT) int limit,
      @RequestParam(defaultValue = Default.Page.SORT_BY) String sortBy) {
    return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
  }

  @Operation(summary = "Find player by user id",
      description = "Retrieve player details by user id. Requires 'ADMIN' role.")
  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping(value = "/users/{userId}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player found by user id"),
      @ApiResponse(responseCode = "404", description = "Player not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<PlayerDTO> findByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(playerService.findByUserId(userId));
  }

  @Operation(summary = "Find player by id",
      description = "Retrieve player profile details by player id. Requires authentication.")
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player profile found by id"),
      @ApiResponse(responseCode = "404", description = "Player not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<PlayerProfileDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(playerService.findById(id));
  }

  @Operation(summary = "Create a new player",
      description = "Create a new player with the provided details. No authentication required.")
  @PostMapping(value = "")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Player created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  public ResponseEntity<PlayerDTO> create(@RequestBody @Valid PlayerCreationDTO playerCreationDTO) {
    return ResponseEntity.ok(playerService.create(playerCreationDTO));
  }

  @Operation(summary = "Update player information",
      description = "Update an existing player's information. Requires authentication.")
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/{id}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Player updated successfully"),
      @ApiResponse(responseCode = "404", description = "Player not found"),
      @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  public ResponseEntity<PlayerProfileDTO> update(@PathVariable long id,
      @RequestBody @Valid PlayerProfileDTO playerProfileDTO) {
    return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
  }

}
