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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{

   private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
   private final String UPLOAD_DIR = "path/files";

   @Override
   public String uploadFile(MultipartFile file){
      if((file == null) || file.isEmpty()){
         return null;
      }

      // Generate a unique file name to avoid overwriting existing files
      String fileName = System.currentTimeMillis() + "__" + file.getOriginalFilename();

      // Create the directory for storing uploaded files if it doesn't exist
      File uploadDir = new File(UPLOAD_DIR);
      if(!uploadDir.exists()){
         boolean created = uploadDir.mkdirs();
         if(!created){
            logger.error("Failed to create directory: {}", UPLOAD_DIR);
            return null;
         }
      }

      Path targetPath = Paths.get(UPLOAD_DIR, fileName);
      try{
         Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
      } catch(IOException e){
         logger.error("Error uploading file: {}", fileName, e);
         return null;
      }

      return fileName;
   }

   @Override
   public Resource downloadFile(String fileName){
      if((fileName == null) || fileName.isEmpty()){
         throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
      }

      Path filePath = Paths.get(UPLOAD_DIR, fileName);
      Resource resource;
      try{
         resource = new UrlResource(filePath.toUri());
         if(!resource.exists() || !resource.isReadable()){
            throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
         }
      } catch(MalformedURLException e){
         throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
      }

      return resource;
   }

   @Override
   public void deleteFile(String fileName){
      try{
         Files.delete(Paths.get(UPLOAD_DIR, fileName));
      } catch(IOException e){
         throw new ResourceNotFoundExceptionCustomize(Collections.singletonMap("fileName", fileName));
      }
   }

}
