package com.krasova.service;

import com.krasova.entity.FileMetadata;
import com.krasova.repository.FileRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by osamo on 3/26/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileScheduledServiceTest {

    @InjectMocks
    private FileScheduledService tested;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private MailService mailService;

    @Test
    public void getLastHourUploads_happyPath() {
        //setup

        List<FileMetadata> fileMetadatas = new ArrayList<>();
        FileMetadata fileMetadata1 = FileMetadata.builder().id(1L).category(RandomStringUtils.random(10))
                .name(RandomStringUtils.random(10)).description(RandomStringUtils.random(10)).uploadDate(new Date()).build();
        fileMetadatas.add(fileMetadata1);
        FileMetadata fileMetadata2 = FileMetadata.builder().id(2L).category(RandomStringUtils.random(10))
                .name(RandomStringUtils.random(10)).description(RandomStringUtils.random(10)).uploadDate(new Date()).build();
        fileMetadatas.add(fileMetadata2);
        List<String> fileNames = new ArrayList<>();
        fileMetadatas.forEach(fileName -> fileNames.add(fileName.getName()));

        when(fileRepository.findByUploadDateBetween(any(Date.class), any(Date.class))).thenReturn(fileMetadatas);

        //execute
        tested.getLastHourUploads();

        //verify
        verify(mailService).notify(fileNames);
    }
}