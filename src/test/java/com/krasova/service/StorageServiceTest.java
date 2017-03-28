package com.krasova.service;

import com.krasova.config.FileManagerProperties;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.when;

/**
 * Created by osamo on 3/27/2017.
 */
public class StorageServiceTest {

    @InjectMocks
    StorageService tested;

    @Mock
    private FileManagerProperties fileManagerProperties;

    @Test(expected = Exception.class)
    public void store_fileIsEmpty() throws Exception {
        // setup
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "".getBytes());
        // execute
        tested.storeFile(multipartFile);
        // verify
        fail();
    }

    @Test(expected = Exception.class)
    public void store_directoryNotExist() throws Exception {
        // setup
        when(fileManagerProperties.getUploadDirectory()).thenReturn(null);
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "test".getBytes());
        // execute
        tested.storeFile(multipartFile);
        // verify
        fail();
    }

    @Test(expected = Exception.class)
    public void init_couldNotInitializeStorage() {
        when(fileManagerProperties.getUploadDirectory()).thenReturn(null);
        // execute
        tested.init();
        // verify
        fail();
    }


}