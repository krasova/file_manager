package com.krasova.service;

import com.krasova.dto.FileMetadataDto;
import com.krasova.entity.FileMetadata;
import com.krasova.repository.FileRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.*;

/**
 * Created by osamo on 3/24/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileManagerServiceTest {

    @InjectMocks
    private FileManagerService tested;

    @Mock
    private FileRepository fileRepository;

    @Before
    public void setUp(){
        tested.init();
    }

    @Test
    public void store_happyPath() throws Exception {
        // setup

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        // execute
        tested.store(multipartFile, category, description);
        // verify
        verify(fileRepository).save(FileMetadata.builder().name(multipartFile.getOriginalFilename()).category(category).description(description).uploadDate(any(Date.class)).build());

    }

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
        fail();
    }

    @Test(expected = Exception.class)
    public void store_directoryNotExist() throws Exception {
        // setup
        tested.deleteAll();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "".getBytes());
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        // execute
        tested.store(multipartFile, category, description);
        // verify
        fail();
    }

    @Test
    public void loadMeatdata_happyPath() throws Exception {
        // setup
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        FileMetadata fileMetadata = FileMetadata.builder().name(multipartFile.getOriginalFilename()).category(category).description(description).build();
        doReturn(fileMetadata).when(fileRepository).findByName(multipartFile.getOriginalFilename());
        // execute
        FileMetadataDto fileMetadataDtoTest = tested.loadMetadata(multipartFile.getOriginalFilename());
        // verify
        assertNotNull(fileMetadataDtoTest);
        assertEquals(fileMetadata.getCategory(),fileMetadataDtoTest.getCategory());
        assertEquals(fileMetadata.getName(),fileMetadataDtoTest.getName());
        assertEquals(fileMetadata.getDescription(),fileMetadataDtoTest.getDescription());
    }

    @Test(expected = Exception.class)
    public void loadMetadata_storageException() throws Exception {
        // setup
        tested.deleteAll();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "123".getBytes());

        doReturn(null).when(fileRepository).findByName(multipartFile.getOriginalFilename());
        // execute
        tested.loadMetadata(multipartFile.getOriginalFilename());
        // verify
        fail();
    }

    @Test(expected = Exception.class)
    public void loadAsResource_storageException() throws Exception {
        // setup
        tested.deleteAll();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "123".getBytes());
        // execute
        tested.loadAsResource(multipartFile.getOriginalFilename());
        // verify
        fail();
    }

    @After
    public void cleanUp(){
        tested.deleteAll();
    }

}