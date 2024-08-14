package com.nvs.service.impl;

import com.nvs.config.exception.ResourceNotFoundExceptionCustomize;
import com.nvs.service.FileService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

  private final String UPLOAD_DIR = "path/files";

  @Override
  public String uploadFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      log.warn("Attempted to upload an empty file");
      return null;
    }

    // Generate a unique file name to avoid overwriting existing files
    String fileName = System.currentTimeMillis() + "__" + file.getOriginalFilename();
    Path targetPath = Paths.get(UPLOAD_DIR, fileName);

    // Create the directory for storing uploaded files if it doesn't exist
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists()) {
      boolean created = uploadDir.mkdirs();
      if (!created) {
        log.error("Failed to create directory: {}", UPLOAD_DIR);
        return null;
      }
      log.info("Created directory for uploads: {}", UPLOAD_DIR);
    }

    try {
      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
      log.info("Successfully uploaded file: {}", fileName);
    } catch (IOException e) {
      log.error("Error uploading file: {}", fileName, e);
      return null;
    }

    return fileName;
  }

  @Override
  public Resource downloadFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      log.warn("File name is null or empty for download request");
      throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
    }

    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    Resource resource;
    try {
      resource = new UrlResource(filePath.toUri());
      if (!resource.exists() || !resource.isReadable()) {
        log.warn("File not found or not readable: {}", fileName);
        throw new ResourceNotFoundExceptionCustomize(
            Collections.singletonMap("fileName", fileName));
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL for file: {}", fileName, e);
      throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
    }

    log.info("Successfully retrieved file for download: {}", fileName);
    return resource;
  }

  @Override
  public void deleteFile(String fileName) {
    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    try {
      Files.delete(filePath);
      log.info("Successfully deleted file: {}", fileName);
    } catch (IOException e) {
      log.error("Error deleting file: {}", fileName, e);
      throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
    }
  }
}
