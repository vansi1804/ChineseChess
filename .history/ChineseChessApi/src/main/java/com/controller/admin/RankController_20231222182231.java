package com.controller.admin;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.data.dto.RankDTO;
import com.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.RANKS)
@PreAuthorize(value = "hasAuthority('ADMIN')")
@Tag(
  name = "Vip",
  description = "Endpoints for managing Vips - requires 'ADMIN' role"
)
@RequiredArgsConstructor
public class RankController {

  private final RankService rankService;

  @Operation(
    summary = "Get all ranks",
    description = "Endpoint to retrieve all ranks",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
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
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
          schema = @Schema(implementation = ErrorMessageResponseDTO.class)
        )
      ),
    }
  )
  @GetMapping(value = "")
  public ResponseEntity<List<RankDTO>> findAll() {
    return ResponseEntity.ok(rankService.findAll());
  }

  @Operation(
    summary = "Find a rank by id",
    description = "Endpoint to retrieve a rank by its id",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
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
  @GetMapping(value = "/{id}")
  public ResponseEntity<RankDTO> findById(
    @Parameter(
      name = "id",
      description = "Id of the rank to retrieve",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "integer", format = "int32")
    ) @PathVariable int id
  ) {
    return ResponseEntity.ok(rankService.findById(id));
  }

  @Operation(
    summary = "Create a rank",
    description = "Endpoint to create a new rank",
   
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
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
        responseCode = "409",
        description = "Conflict",
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
  @PostMapping(value = "")
  public ResponseEntity<RankDTO> create(@RequestBody @Valid RankDTO rankDTO) {
    return ResponseEntity.ok(rankService.create(rankDTO));
  }

  @Operation(
    summary = "Update a rank",
    description = "Endpoint to update an existing rank",
    parameters = {
      @Parameter(
        name = "id",
        description = "Id of the rank to update",
        required = true,
        in = ParameterIn.PATH,
        schema = @Schema(type = "integer", format = "int32")
      ),
    },
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Rank data to update",
      required = true,
      content = @Content(schema = @Schema(implementation = RankDTO.class))
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(schema = @Schema(implementation = RankDTO.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Bad Request",
        content = @Content(
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
        responseCode = "409",
        description = "Conflict",
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
  @PutMapping(value = "/{id}")
  public ResponseEntity<RankDTO> update(
    @PathVariable int id,
    @RequestBody @Valid RankDTO rankDTO
  ) {
    return ResponseEntity.ok(rankService.update(id, rankDTO));
  }
}
