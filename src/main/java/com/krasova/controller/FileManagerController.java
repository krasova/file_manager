package com.krasova.controller;

import com.krasova.dto.FileMetadataDto;
import com.krasova.exception.StorageFileNotFoundException;
import com.krasova.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by osamo on 3/23/2017.
 */
@Controller
public class FileManagerController {

    @Autowired
    private FileManagerService fileManagerService;

    @PostMapping("/")
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("category") String category, @RequestParam("description") String description) {
        fileManagerService.store(file, category, description);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metadata/{filename:.+}")
    public ResponseEntity<FileMetadataDto> handleGetMetadata(@PathVariable String filename) {
        return ResponseEntity.ok(fileManagerService.loadMetadata(filename));
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = fileManagerService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
