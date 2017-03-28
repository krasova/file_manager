package com.krasova.repository;

import com.krasova.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by osamo on 3/23/2017.
 */
@Repository
public interface FileRepository extends JpaRepository<FileMetadata, Long> {
    FileMetadata findByName(String name);
    List<FileMetadata> findByUploadDateBetween(Date startDate, Date endDate);
}
