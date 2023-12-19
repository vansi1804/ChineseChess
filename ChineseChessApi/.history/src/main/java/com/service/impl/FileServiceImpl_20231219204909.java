package com.service.impl;

import com.config.exception.ResourceNotFoundExceptionCustomize;
import com.service.FileService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private String UPLOAD_DIR = "path/files";

  @Override
  public String uploadFile(MultipartFile file) {
    if ((file == null) || file.isEmpty()) {
      return null;
    }

    // Generate a unique file name to avoid overwriting existing files
    String fileName =
      System.currentTimeMillis() + "_" + file.getOriginalFilename();

    // Create the directory for storing uploaded files if it doesn't exist
    File uploadDir = new File(UPLOAD_DIR);
    if (!uploadDir.exists()) {
      uploadDir.mkdirs();
    }

    Path targetPath = Paths.get(UPLOAD_DIR, fileName);
    try {
      Files.copy(
        file.getInputStream(),
        targetPath,
        StandardCopyOption.REPLACE_EXISTING
      );
    } catch (IOException e) {
      e.printStackTrace();
    }

    return fileName;
  }

  @Override
  public Resource downloadFile(String fileName) {
    if ((fileName == null) || fileName.isEmpty()) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap("fileName", fileName)
      );
    }

    Path filePath = Paths.get(UPLOAD_DIR, fileName);
    Resource resource;
    try {
      resource = new UrlResource(filePath.toUri());
      if (!resource.exists() || !resource.isReadable()) {
        throw new ResourceNotFoundExceptionCustomize(
          Collections.singletonMap("fileName", fileName)
        );
      }
    } catch (MalformedURLException e) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap("fileName", fileName)
      );
    }

    return resource;
  }

  @Override
  public void deleteFile(String fileName) {
    try {
      Files.delete(Paths.get(UPLOAD_DIR, fileName));
    } catch (IOException e) {
      throw new ResourceNotFoundExceptionCustomize(
        Collections.singletonMap("fileName", fileName)
      );
    }
  }
}
