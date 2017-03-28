package com.krasova.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by osamo on 3/23/2017.
 */
@Data
@AllArgsConstructor
@Builder
public class FileMetadataDto {
    private Long attachmentId;

    private String name;

    private String category;

    private String description;
}
