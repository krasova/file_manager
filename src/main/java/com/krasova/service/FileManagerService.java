package com.krasova.service;

import com.krasova.dto.FileMetadataDto;
import com.krasova.entity.FileMetadata;
import com.krasova.exception.StorageException;
import com.krasova.exception.StorageFileNotFoundException;
import com.krasova.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by osamo on 3/23/2017.
 */
@Service
public class FileManagerService {

    private final Path rootLocation = Paths.get("upload");
    //private final Path rootLocation;

    @Autowired
    private FileRepository fileRepository;

//    @Autowired
//    public FileManagerService(FileManagerProperties fileManagerProperties) {
//        this.rootLocation = Paths.get(fileManagerProperties.getUploadDirectory());
//    }

    @Transactional
    public void store(MultipartFile file, String category, String description) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            fileRepository.save(FileMetadata.builder().name(file.getOriginalFilename()).category(category).description(description).build());
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }


    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public FileMetadataDto loadMetadata(String filename) {
        FileMetadata fileMetadata = fileRepository.findByName(filename);
        if (fileMetadata != null) {
            return toFileMetadataDto(fileMetadata);
        } else {
            throw new StorageException("Metadata doesn't exist for " + filename);
        }
    }

    private FileMetadataDto toFileMetadataDto(FileMetadata fileMetadata) {
            return FileMetadataDto.builder().attachmentId(fileMetadata.getId()).category(fileMetadata.getCategory()).name(fileMetadata.getName()).description(fileMetadata.getDescription()).build();
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
}
