package com.controller;

import com.common.ApiUrl;
import com.data.dto.ErrorMessageResponseDTO;
import com.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(ApiUrl.FILES)
@Tag(name = "File", description = "Endpoints for managing files")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @Operation(
    summary = "Upload a file",
    description = "Endpoint to upload a file",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = String.class)
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
  @PostMapping(value = "/upload")
  public ResponseEntity<String> uploadFile(
    @Parameter(
      description = "File to be uploaded",
      required = true,
      content = @Content(
        mediaType = "multipart/form-data",
        schema = @Schema(type = "string", format = "binary")
      )
    ) @RequestParam MultipartFile file
  ) {
    return ResponseEntity.ok().body(fileService.uploadFile(file));
  }

  @Operation(
    summary = "Download file",
    description = "Endpoint to download a file",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(mediaType = "application/octet-stream")
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
  @GetMapping(value = "/download/{fileName}")
  public ResponseEntity<Resource> downloadFile(
    @Parameter(
      description = "Name of the file to download",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "string")
    ) @PathVariable String fileName
  ) throws IOException {
    Resource resource = fileService.downloadFile(fileName);
    String originalFilename = obtainOriginalFileName(resource);
    String mediaType = Files.probeContentType(resource.getFile().toPath());

    return ResponseEntity
      .ok()
      .contentType(MediaType.parseMediaType(mediaType))
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + originalFilename + "\""
      )
      .body(resource);
  }

  @Operation(
    summary = "Display file",
    description = "Endpoint to display a file on the web",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully",
        content = @Content(mediaType = "application/octet-stream")
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
  @GetMapping(value = "/display/{fileName}") // view on web
  public ResponseEntity<byte[]> displayFile(
    @Parameter(
      description = "Name of the file to download",
      required = true,
      in = ParameterIn.PATH,
      schema = @Schema(type = "string")
    ) @PathVariable String fileName
  ) throws IOException {
    Resource resource = fileService.downloadFile(fileName);
    String originalFilename = obtainOriginalFileName(resource);
    String mediaType = detectMediaType(originalFilename);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(mediaType));
    headers.setContentDisposition(
      ContentDisposition.inline().filename(originalFilename).build()
    );

    byte[] fileBytes;
    try (InputStream inputStream = resource.getInputStream()) {
      fileBytes = IOUtils.toByteArray(inputStream);
    }

    return ResponseEntity.ok().headers(headers).body(fileBytes);
  }

  private String obtainOriginalFileName(Resource resource) {
    String originalFilename = resource.getFilename();
    if (originalFilename != null && originalFilename.contains("_")) {
      originalFilename = originalFilename.split("_", 2)[1];
    }
    return originalFilename;
  }

  private String detectMediaType(String filename) {
    return new Tika().detect(filename);
  }
}
