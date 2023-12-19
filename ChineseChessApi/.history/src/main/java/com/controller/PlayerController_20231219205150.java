package com.controller;

import com.common.ApiUrl;
import com.common.Default;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.PLAYERS)
@RequiredArgsConstructor
public class PlayerController {

  private final PlayerService playerService;

  @Operation(
    summary = "Find all players",
    description = "Retrieve a paginated list of all players",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<Page<PlayerDTO>> findAll(
    @RequestParam(
      name = "no",
      required = false,
      defaultValue = Default.Page.NO
    ) int no,
    @RequestParam(
      name = "limit",
      required = false,
      defaultValue = Default.Page.LIMIT
    ) int limit,
    @RequestParam(
      name = "sort-by",
      required = false,
      defaultValue = Default.Page.SORT_BY
    ) String sortBy
  ) {
    return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
  }

  @Operation(
    summary = "Find player by user ID",
    description = "Retrieve a player by their user ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "/users/id={userId}")
  public ResponseEntity<PlayerDTO> findByUserId(@PathVariable long userId) {
    return ResponseEntity.ok(playerService.findByUserId(userId));
  }

  @Operation(
    summary = "Find player by ID",
    description = "Retrieve a player by their ID",
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{id}")
  public ResponseEntity<PlayerProfileDTO> findById(@PathVariable long id) {
    return ResponseEntity.ok(playerService.findById(id));
  }

  @Operation(
    summary = "Create a player",
    description = "Create a new player",
    requestBody = @RequestBody(
      description = "Player data to create",
      required = true,
      content = @Content(
        schema = @Schema(implementation = PlayerCreationDTO.class)
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PostMapping(value = "")
  public ResponseEntity<PlayerDTO> create(
    @RequestBody @Valid PlayerCreationDTO playerCreationDTO
  ) {
    return ResponseEntity.ok(playerService.create(playerCreationDTO));
  }

  @Operation(
    summary = "Update player profile",
    description = "Update a player's profile",
    requestBody = @RequestBody(
      description = "Player data to update",
      required = true,
      content = @Content(
        schema = @Schema(implementation = PlayerProfileDTO.class)
      )
    ),
    responses = {
      @ApiResponse(responseCode = "200", description = "Successfully"),
    }
  )
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/{id}")
  public ResponseEntity<PlayerProfileDTO> update(
    @PathVariable long id,
    @RequestBody @Valid PlayerProfileDTO playerProfileDTO
  ) {
    return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
  }
}
