package com.controller;

import com.common.ApiUrl;
import com.common.Default;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.player.PlayerCreationDTO;
import com.data.dto.player.PlayerDTO;
import com.data.dto.player.PlayerProfileDTO;
import com.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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

  @Operation(
    summary = "Get all",
    description = "Endpoint to retrieve all players",
  )
  @PreAuthorize(value = "hasAuthority('ADMIN')")
  @GetMapping(value = "")
  public ResponseEntity<Page<PlayerDTO>> findAll(
    @RequestParam(      defaultValue = Default.Page.NO    ) int no,
    @RequestParam(      defaultValue = Default.Page.LIMIT    ) int limit,
    @RequestParam(
      defaultValue = Default.Page.SORT_BY
    ) String sortBy
  ) {
    return ResponseEntity.ok(playerService.findAll(no, limit, sortBy));
  }

  @Operation(
    summary = "Find by user's id",
    description = "Endpoint to find a player by user's id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PlayerDTO.class)
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
  @GetMapping(value = "/users/{userId}")
  public ResponseEntity<PlayerDTO> findByUserId(
    @Parameter(
      name = "id",
      description = "Id of user",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long userId
  ) {
    return ResponseEntity.ok(playerService.findByUserId(userId));
  }

  @Operation(
    summary = "Find by id",
    description = "Endpoint to find a player by id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PlayerProfileDTO.class)
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
  @GetMapping(value = "/{id}")
  public ResponseEntity<PlayerProfileDTO> findById(
    @Parameter(
      name = "id",
      description = "Id of player",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id
  ) {
    return ResponseEntity.ok(playerService.findById(id));
  }

  @Operation(
    summary = "Create",
    description = "Endpoint to create new player",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PlayerDTO.class)
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
  @PostMapping(value = "")
  public ResponseEntity<PlayerDTO> create(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Player data to create",
      required = true,
      content = @Content(
        schema = @Schema(implementation = PlayerCreationDTO.class)
      )
    ) @RequestBody @Valid PlayerCreationDTO playerCreationDTO
  ) {
    return ResponseEntity.ok(playerService.create(playerCreationDTO));
  }

  @Operation(
    summary = "Update",
    description = "Endpoint to update player's information",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PlayerDTO.class)
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
  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/{id}")
  public ResponseEntity<PlayerProfileDTO> update(
    @Parameter(
      name = "id",
      description = "Id of player",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int64")
    ) @PathVariable long id,
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Player data to update",
      required = true,
      content = @Content(
        schema = @Schema(implementation = PlayerProfileDTO.class)
      )
    ) @RequestBody @Valid PlayerProfileDTO playerProfileDTO
  ) {
    return ResponseEntity.ok(playerService.update(id, playerProfileDTO));
  }
}
