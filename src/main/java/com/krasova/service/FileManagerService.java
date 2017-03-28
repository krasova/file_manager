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
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Date;

/**
 * Created by osamo on 3/23/2017.
 */
@Service
public class FileManagerService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public void store(MultipartFile file, String category, String description) {
        storageService.storeFile(file);
        fileRepository.save(FileMetadata.builder().name(file.getOriginalFilename()).category(category).description(description).uploadDate(new Date()).build());
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

    public Resource loadAsResource(String filename) {
        try {
            Path file = storageService.load(filename);
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
