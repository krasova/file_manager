package com.krasova.controller;

import com.krasova.dto.FileMetadataDto;
import com.krasova.exception.StorageFileNotFoundException;
import com.krasova.service.FileManagerService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by osamo on 3/24/2017.
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FileManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileManagerService fileManagerService;

    @Test
    public void handleFileUpload_happyPath() throws Exception {
        String category = RandomStringUtils.random(10);
        String description = RandomStringUtils.random(10);
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "resources.txt", "text/plain", "resources".getBytes());
        this.mockMvc.perform(fileUpload("/").file(multipartFile).param("category", category).param("description",description))
                .andExpect(status().isOk());
    }

    @Test
    public void handleGetMetadata_happyPath() throws Exception {
        String filename = RandomStringUtils.random(10) + "." + RandomStringUtils.random(3);
        FileMetadataDto fileMetadataDto = null;

        Mockito.when(fileManagerService.loadMetadata(filename))
                .thenReturn(fileMetadataDto);

        mockMvc.perform(get("/metadata/"+filename))
                .andExpect(status().isOk());
    }

    @Test
    public void serveFile_fileNotFound() throws Exception {
        given(this.fileManagerService.loadAsResource("resources.txt"))
                .willThrow(StorageFileNotFoundException.class);

        this.mockMvc.perform(get("/files/resources.txt"))
                .andExpect(status().isNotFound());
    }


}