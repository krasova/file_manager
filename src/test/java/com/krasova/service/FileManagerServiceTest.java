package com.krasova.service;

import com.krasova.entity.FileMetadata;
import com.krasova.repository.FileRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

/**
 * Created by osamo on 3/24/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileManagerServiceTest {

    @InjectMocks
    private FileManagerService tested;

    @Test(expected = Exception.class)
    public void store_fileIsEmpty() throws Exception {
        // setup
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "".getBytes());
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        // execute
        tested.store(multipartFile, category, description);
        // verify
        Assert.fail();
    }

    @Test
    public void loadMetadata_happyPath() throws Exception {
        // setup
        FileRepository fileRepository = mock(FileRepository.class);
        ReflectionTestUtils.setField(tested,
                "fileRepository",
                fileRepository);
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "123".getBytes());
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        FileMetadata fileMetadata = FileMetadata.builder().name(multipartFile.getOriginalFilename()).category(category).description(description).build();

        // execute
        tested.loadMetadata(multipartFile.getOriginalFilename());
        // verify
        verify(fileRepository).findByName(multipartFile.getOriginalFilename());
    }

    @Test(expected = Exception.class)
    public void loadMetadata_storageException() throws Exception {
        // setup
        FileRepository fileRepository = mock(FileRepository.class);
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "123".getBytes());

        doReturn(null).when(fileRepository).findByName(multipartFile.getOriginalFilename());
        // execute
        tested.loadMetadata(multipartFile.getOriginalFilename());
        // verify
        fail();
    }

}