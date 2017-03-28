package com.krasova.service;

import com.krasova.config.FileManagerProperties;
import com.krasova.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by osamo on 3/27/2017.
 */
@Service
public class StorageService {

    private Path rootLocation;

    @Autowired
    private FileManagerProperties fileManagerProperties;

    @PostConstruct
    public void setRootLocation(){
        this.rootLocation = Paths.get(fileManagerProperties.getUploadDirectory());
    }

    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public void storeFile(MultipartFile file)  {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

}
