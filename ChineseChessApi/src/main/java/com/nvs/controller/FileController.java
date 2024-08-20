package com.nvs.controller;

import com.nvs.common.ApiUrl;
import com.nvs.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping(ApiUrl.FILES)
@Tag(name = "File", description = "Endpoints for managing files")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  @Operation(summary = "Upload a file", description = "Upload a file to the server.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid file or upload request"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @PostMapping(value = "/upload")
  public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
    log.debug("-- Uploading file: {}", file.getOriginalFilename());
    String response = fileService.uploadFile(file);
    log.info("-- File uploaded successfully: {}", file.getOriginalFilename());
    return ResponseEntity.ok().body(response);
  }

  @Operation(summary = "Download a file", description = "Download a file from the server.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "File downloaded successfully"),
      @ApiResponse(responseCode = "404", description = "File not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "/download/{fileName}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
    log.debug("-- Downloading file: {}", fileName);
    Resource resource = fileService.downloadFile(fileName);
    String originalFilename = obtainOriginalFileName(resource);
    String mediaType = Files.probeContentType(resource.getFile().toPath());
    log.info("-- File downloaded successfully: {}", originalFilename);

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(mediaType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + originalFilename + "\"").body(resource);
  }

  @Operation(summary = "Display a file", description = "Display a file inline on the web.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "File displayed successfully"),
      @ApiResponse(responseCode = "404", description = "File not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping(value = "/display/{fileName}")
  public ResponseEntity<byte[]> displayFile(@PathVariable String fileName) throws IOException {
    log.debug("-- Displaying file: {}", fileName);
    Resource resource = fileService.downloadFile(fileName);
    String originalFilename = obtainOriginalFileName(resource);
    String mediaType = detectMediaType(originalFilename);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(mediaType));
    headers.setContentDisposition(ContentDisposition.inline().filename(originalFilename).build());

    byte[] fileBytes;
    try (InputStream inputStream = resource.getInputStream()) {
      fileBytes = IOUtils.toByteArray(inputStream);
    }

    log.info("-- File displayed successfully: {}", originalFilename);
    return ResponseEntity.ok().headers(headers).body(fileBytes);
  }

  private String obtainOriginalFileName(Resource resource) {
    String originalFilename = resource.getFilename();
    if (originalFilename != null && originalFilename.contains("__")) {
      originalFilename = originalFilename.split("__", 2)[1];
    }
    return originalFilename;
  }

  private String detectMediaType(String filename) {
    return new Tika().detect(filename);
  }
}
