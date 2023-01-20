package com.recruitment.controller;

import com.recruitment.base.ExperienceBaseTest;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.exception.DocumentInvalidException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.ExperienceMapper;
import com.recruitment.service.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExperienceController.class)
class ExperienceControllerFunctionalTest extends ExperienceBaseTest {

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExperienceService experienceService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldCreateExperience() throws Exception {
        given(experienceService.createExperience(any(ExperienceDTO.class))).willReturn(experienceDTO);

        this.mockMvc.perform(post("/api/experiences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(experienceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidateId").value("1"));
    }

    @Test
    @Disabled("Disabled until finding the solution for sending the MultipartFile")
    void shouldReturnErrorWhenDocumentFormatIsInvalid() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "file", MediaType.TEXT_PLAIN_VALUE,
//                "Hello World".getBytes());
//
//        given(experienceService.uploadExperience(experienceDTO, document, documentType))
//                .willThrow(new DocumentInvalidException("The document format is not supported!"));
//        MultiMap<String, String> params = new ArrayListValuedHashMap<String, String>();
//        params.put("experience", experienceId1.toString());
//        params.put("docType", DocumentType.CV.toString());
//
//        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/experiences/upload")
//                .file(file)
//                .params(params)
//                .with(request -> {
//                    request.setMethod("PATCH");
//                    return request;
//                }))
//                .andExpect(status().isConflict())
//                .andExpect(jsonPath("$.message").value("The document format is not supported!"));
    }

    @Test
    @Disabled
    void shouldReturnErrorWhenCandidateIdIsInvalid() throws Exception {

        HashMap<String, String> contentTypeParams = new HashMap<>();
        contentTypeParams.put("experience.candidateId", String.valueOf(EXPERIENCE_ID));
        contentTypeParams.put("docType", String.valueOf(documentType));
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        doThrow(new ResourceNotFoundException("User with id: 100 not found."))
                .when(experienceService).uploadExperience(any(), any(), any());
        this.mockMvc.perform(patch("/api/experiences/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(mapperBuilder.build().writeValueAsString(ExperienceMapper.buildDTO(experienceId1))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with id: 100 not found."));
    }

    @Test
    void getAllExperiences() throws Exception {
        given(experienceService.getAllExperiences()).willReturn(Collections.singletonList(experienceDTO));

        this.mockMvc.perform(get("/api/experiences").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].candidateId").value("1"));
    }

    @Test
    void shouldReturnErrorWhenCvNotFoundForDownload() throws Exception {
        doThrow(new ResourceNotFoundException("Experience with id:" + EXPERIENCE_ID + " does not have a cv document in the database"))
                .when(experienceService).getDocumentDetails(EXPERIENCE_ID, documentType);
        this.mockMvc.perform(get("/api/experiences/download/1")
                .param("docType", String.valueOf(documentType))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Experience with id:" + EXPERIENCE_ID + " does not have a cv document in the database"));
    }

    @Test
    void shouldReturnErrorWhenExperienceNotFoundForDownload() throws Exception {
        doThrow(new ResourceNotFoundException("Experience with id: 100 not found."))
                .when(experienceService).getDocumentDetails(100L, documentType);
        this.mockMvc.perform(get("/api/experiences/download/100")
                .param("docType", String.valueOf(documentType))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Experience with id: 100 not found."));
    }
}
