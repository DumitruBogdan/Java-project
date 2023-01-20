package com.recruitment.controller;

import com.recruitment.base.CandidateBaseTest;
import com.recruitment.domain.Candidate;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.domain.enums.HiredStatus;
import com.recruitment.dto.CandidateDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CandidateMapper;
import com.recruitment.service.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CandidateController.class)
class CandidateControllerFunctionalTest extends CandidateBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldReturnCandidateDtoWhenCreatingCandidateSuccessfully() throws Exception {

        given(candidateService.createCandidate(any())).willReturn(candidateResponseDTO);

        this.mockMvc.perform(post("/api/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(candidateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.email").value("ana.popescu@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("998-253-8290"))
                .andExpect(jsonPath("$.country").value("Romania"))
                .andExpect(jsonPath("$.address").value("Alba Iulia, Strada Albastrelelor nr. 9"))
                .andExpect(jsonPath("$.username").value("anapopescu"))
                .andExpect(jsonPath("$.lastLoginDate").value("2020-03-15T14:15:10"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    void shouldReturnErrorWhenDuplicateUsername() throws Exception {
        given(candidateService.createCandidate(CandidateMapper.buildDTO(candidateId2))).willThrow(new EntityAlreadyExistsException("User already exists!"));

        this.mockMvc.perform(post("/api/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(CandidateMapper.buildDTO(candidateId2))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists!"));
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {
        this.mockMvc.perform(delete("/api/candidates/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(candidateService).deleteCandidate(1L);
    }

    @Test
    void shouldReturnErrorWhenCannotFindCandidateToDelete() throws Exception {
        doThrow(new ResourceNotFoundException("Candidate does not exist!")).when(candidateService).deleteCandidate(99L);
        this.mockMvc.perform(delete("/api/candidates/99").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Candidate does not exist!"));
    }

    @Test
    void shouldReturnStatusOkWhenGettingAllCandidates() throws Exception {
        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(candidateId2);
        candidateList.add(candidateId3);
        List<CandidateDTO> candidateDTOS = CandidateMapper.buildDTOs(candidateList);

        given(candidateService.getAllCandidates()).willReturn(candidateDTOS);

        this.mockMvc.perform(get("/api/candidates").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("2"));
    }

    @Test
    void shouldReturnStatusOkWhenGettingCandidateById() throws Exception {
        given(candidateService.getCandidateById(2L)).willReturn(CandidateMapper.buildDTO(candidateId2));
        this.mockMvc.perform(get("/api/candidates/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.email").value("ana.popescu@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("998-253-8290"))
                .andExpect(jsonPath("$.country").value("Romania"))
                .andExpect(jsonPath("$.address").value("Alba Iulia, Strada Albastrelelor nr. 9"))
                .andExpect(jsonPath("$.username").value("anapopescu"))
                .andExpect(jsonPath("$.accountStatus").value("ACTIVE"));
    }

    @Test
    void shouldReturnStatusOkWhenUpdatingExistingCandidate() throws Exception {
        CandidateDTO candidateDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Popescu")
                .gender("Female")
                .email("ana.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("anapopescu")
                .accountStatus(AccountStatus.ACTIVE)
                .hiredStatus(null)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        CandidateDTO candidateResponseDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Maria")
                .lastName("Popescu")
                .gender("Female")
                .email("maria.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("mariapopescu")
                .accountStatus(AccountStatus.ARCHIVED)
                .hiredStatus(HiredStatus.GO)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        given(candidateService.updateCandidate(candidateDTO)).willReturn(candidateResponseDTO);

        this.mockMvc.perform(put("/api/candidates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(candidateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.email").value("maria.popescu@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("998-253-8290"))
                .andExpect(jsonPath("$.country").value("Romania"))
                .andExpect(jsonPath("$.address").value("Alba Iulia, Strada Albastrelelor nr. 9"))
                .andExpect(jsonPath("$.username").value("mariapopescu"))
                .andExpect(jsonPath("$.lastLoginDate").value("2020-03-15T14:15:10"))
                .andExpect(jsonPath("$.accountStatus").value("ARCHIVED"))
                .andExpect(jsonPath("$.hiredStatus").value("GO"));
    }

    @Test
    void shouldReturnErrorWhenCannotFindCandidateToUpdate() throws Exception {
        doThrow(new ResourceNotFoundException("Candidate with id: 99 not found.")).when(candidateService).updateCandidate(any());
        this.mockMvc.perform(put("/api/candidates/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(candidateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Candidate with id: 99 not found."));
    }
}
