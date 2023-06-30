package com.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.tika.Tika;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.service.FileService;

@RestController
@RequestMapping("api/files")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/download/fileName={fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
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
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFilename + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/display/fileName={fileName}") // view on web
    public ResponseEntity<byte[]> displayFile(@PathVariable String fileName) throws IOException {
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
            headers.setContentDisposition(ContentDisposition.inline().filename(originalFilename).build());

            InputStream inputStream = resource.getInputStream();
            byte[] fileBytes = IOUtils.toByteArray(inputStream);
            inputStream.close();

            return ResponseEntity.ok().headers(headers).body(fileBytes);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
