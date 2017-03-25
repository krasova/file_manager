package com.krasova.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("fileManager")
public class FileManagerProperties {
    /**
     * Folder location for storing files
     */
    private String uploadDirectory;

}
