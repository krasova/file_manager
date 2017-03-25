package com.krasova.service;

import com.krasova.entity.FileMetadata;
import com.krasova.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by osamo on 3/24/2017.
 */
@Service
@Slf4j
public class FileScheduledService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private MailService mailService;

    @Scheduled(cron = "0 * * * *")
    public void getLastHourUploads() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            List<FileMetadata> fileMetadatas = fileRepository.findAfterUploadDate(calendar.getTime());
            List<String> fileNames = new ArrayList<>();
            if (fileMetadatas.size() > 0) {
                fileMetadatas.forEach(fileName -> fileNames.add(fileName.getName()));
                mailService.notify(fileNames);
            }

        } catch (Exception e) {
            log.error("Failed to get last hour uploads",
                    e);
        }
    }
}
