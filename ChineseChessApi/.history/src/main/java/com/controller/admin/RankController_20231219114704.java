package com.controller.admin;

import com.common.ApiUrl;
import com.data.dto.RankDTO;
import com.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.RANKS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@Tag(name = "Rank", description = "CRUD Rank - requires 'ADMIN' role")
public class RankController {

  private final RankService rankService;

  public RankController(RankService rankService) {
    this.rankService = rankService;
  }

  /**
   * Retrieves all ranks available in the system.
   *
   * @return A list of all ranks.
   */
  @Operation(
    summary = "Find all ranks",
    description = "Retrieves all ranks available in the system",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved ranks",
        content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = RankDTO.class))
        )
      ),
    }
  )
  @GetMapping(value = "")
  public ResponseEntity<List<RankDTO>> findAll() {
    return ResponseEntity.ok(rankService.findAll());
  }

  /**
   * Retrieves a rank by its ID.
   *
   * @param id The ID of the rank to retrieve.
   * @return The requested rank.
   */
  @Operation(
    summary = "Find a rank by ID",
    description = "Retrieves a rank by its unique ID",
    parameters = {
      @Parameter(
        name = "id",
        description = "ID of the rank to retrieve",
        required = true,
        in = ParameterIn.PATH,
        schema = @Schema(type = "integer", format = "int32")
      ),
    },
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved the rank",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Rank not found"),
    }
  )
  @GetMapping(value = "/{id}")
  public ResponseEntity<RankDTO> findById(@PathVariable int id) {
    return ResponseEntity.ok(rankService.findById(id));
  }

  /**
   * Create a new rank.
   *
   * @param rankDTO The data of the rank to be created.
   * @return The newly created rank.
   */
  @Operation(
    summary = "Create a rank",
    description = "Create a new rank",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Rank data to create",
      required = true,
      content = @Content(schema = @Schema(implementation = RankDTO.class))
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully created the rank",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "409", description = "Conflict"),
    }
  )
  @PostMapping(value = "")
  public ResponseEntity<RankDTO> create(@RequestBody @Valid RankDTO rankDTO) {
    return ResponseEntity.ok(rankService.create(rankDTO));
  }

  /**
   * Updates an existing rank by ID.
   *
   * @param id      The ID of the rank to be updated.
   * @param rankDTO The updated data of the rank.
   * @return The updated rank.
   */
  @Operation(
    summary = "Update a rank",
    description = "Updates an existing rank by its ID",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Rank data to update",
      required = true,
      content = @Content(schema = @Schema(implementation = RankDTO.class))
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully updated the rank",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(responseCode = "401", description = "Unauthenticated"),
      @ApiResponse(responseCode = "403", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "Rank not found"),
      @ApiResponse(responseCode = "409", description = "Conflict"),
    }
  )
  @PutMapping(value = "/{id}")
  public ResponseEntity<RankDTO> update(
    @PathVariable int id,
    @RequestBody @Valid RankDTO rankDTO
  ) {
    return ResponseEntity.ok(rankService.update(id, rankDTO));
  }
}
