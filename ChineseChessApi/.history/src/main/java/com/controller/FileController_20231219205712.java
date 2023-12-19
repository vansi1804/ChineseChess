package com.controller;

import com.common.ApiUrl;
import com.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Files", description = "Endpoints for managing matches")
@RequiredArgsConstructor
public class FileController {

  private FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @Operation(
    description = "Upload file",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully uploaded file",
        content = @Content(
          mediaType = "text/plain",
          schema = @Schema(implementation = String.class)
        )
      ),
    }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully uploaded file"
      ),
      @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    }
  )
  @PostMapping(value = "/upload")
  public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
    return ResponseEntity.ok(fileService.uploadFile(file));
  }

  @Operation(
    description = "Download file",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully downloaded file",
        content = @Content(mediaType = "application/octet-stream")
      ),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    }
  )
  @GetMapping(value = "/download/fileName={fileName}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName)
    throws IOException {
    Resource resource = fileService.downloadFile(fileName);
    if (resource != null) {
      String originalFilename = resource.getFilename();
      if (originalFilename != null) {
        String[] filenameParts = originalFilename.split("_", 2);
        if (filenameParts.length > 1) {
          originalFilename = filenameParts[1];
        }
      }
      String mediaType = Files.probeContentType(resource.getFile().toPath());
      return ResponseEntity
        .ok()
        .contentType(MediaType.parseMediaType(mediaType))
        .header(
          HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=\"" + originalFilename + "\""
        )
        .body(resource);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @Operation(
    description = "Display file on the web",
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved file",
        content = @Content(mediaType = "application/octet-stream")
      ),
      @ApiResponse(responseCode = "404", description = "Not found"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    }
  )
  @GetMapping(value = "/display/fileName={fileName}") // view on web
  public ResponseEntity<byte[]> displayFile(@PathVariable String fileName)
    throws IOException {
    Resource resource = fileService.downloadFile(fileName);
    if (resource != null) {
      String originalFilename = resource.getFilename();
      if (originalFilename != null) {
        String[] filenameParts = originalFilename.split("_", 2);
        if (filenameParts.length > 1) {
          originalFilename = filenameParts[1];
        }
      }

      Tika tika = new Tika();
      String mediaType = tika.detect(originalFilename);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType(mediaType));
      headers.setContentDisposition(
        ContentDisposition.inline().filename(originalFilename).build()
      );

      InputStream inputStream = resource.getInputStream();
      byte[] fileBytes = IOUtils.toByteArray(inputStream);
      inputStream.close();

      return ResponseEntity.ok().headers(headers).body(fileBytes);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
