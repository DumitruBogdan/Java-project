package com.recruitment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.base.InterviewScheduleBaseTest;
import com.recruitment.dto.InterviewScheduleDTO;
import com.recruitment.exception.RequestValidationException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.InterviewScheduleMapper;
import com.recruitment.service.InterviewScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(InterviewScheduleController.class)
class InterviewScheduleControllerFunctionalTest extends InterviewScheduleBaseTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private InterviewScheduleService interviewScheduleService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldDeleteInterviewSchedule() throws Exception {
        mvc.perform(delete("/api/interviews/{id}", hrId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(interviewScheduleService).deleteInterviewScheduleById(hrId);
    }

    @Test
    void shouldReturnErrorWhenCannotFindInterviewScheduleToDelete() throws Exception {
        doThrow(new ResourceNotFoundException("Interview does not exist!")).when(interviewScheduleService).deleteInterviewScheduleById(hrId);
        this.mvc.perform(delete("/api/interviews/{id}", hrId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Interview does not exist!"));
    }

    @Test
    void shouldReturnStatusOkWhenCreatingInterview() throws Exception {
        given(interviewScheduleService.createInterview(hrInterviewDTO)).willReturn(InterviewScheduleMapper.buildDTO(hrInterview));
        mvc.perform(post("/api/interviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hrInterviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hrId))
                .andExpect(jsonPath("$.startDate").value("2022-03-15T14:15:00"))
                .andExpect(jsonPath("$.endDate").value("2022-03-15T15:15:00"))
                .andExpect(jsonPath("$.candidateId").value(hrInterviewDTO.getCandidateId()))
                .andExpect(jsonPath("$.appliedDepartmentId").value(hrInterviewDTO.getAppliedDepartmentId()))
                .andExpect(jsonPath("$.interviewType").value(hrInterviewDTO.getInterviewType().name()));
        verify(interviewScheduleService).createInterview(hrInterviewDTO);
    }

    @Test
    void shouldReturnStatusOkWhenGettingInterviewById() throws Exception {
        given(interviewScheduleService.getInterviewById(hrId)).willReturn(InterviewScheduleMapper.buildDTO(hrInterview));
        mvc.perform(get("/api/interviews/{id}", hrId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hrInterviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hrId))
                .andExpect(jsonPath("$.startDate").value("2022-03-15T14:15:00"))
                .andExpect(jsonPath("$.endDate").value("2022-03-15T15:15:00"))
                .andExpect(jsonPath("$.candidateId").value(hrInterviewDTO.getCandidateId()))
                .andExpect(jsonPath("$.appliedDepartmentId").value(hrInterviewDTO.getAppliedDepartmentId()))
                .andExpect(jsonPath("$.interviewType").value(hrInterviewDTO.getInterviewType().name()));
        verify(interviewScheduleService).getInterviewById(hrId);
    }

    @Test
    void shouldUpdateInterviewSchedule() throws Exception {
        given(interviewScheduleService.updateInterviewSchedule(any(InterviewScheduleDTO.class))).willReturn(technicalInterviewDTO);

        mvc.perform(put("/api/interviews/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(technicalInterviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(technicalInterviewDTO.getId()))
                .andExpect(jsonPath("$.startDate").value("2022-05-17T11:00:00"))
                .andExpect(jsonPath("$.endDate").value("2022-05-17T12:00:00"))
                .andExpect(jsonPath("$.candidateId").value(technicalInterviewDTO.getCandidateId()))
                .andExpect(jsonPath("$.appliedDepartmentId").value(technicalInterviewDTO.getAppliedDepartmentId()))
                .andExpect(jsonPath("$.interviewType").value(technicalInterviewDTO.getInterviewType().name()));
        verify(interviewScheduleService).updateInterviewSchedule(technicalInterviewDTO);
    }

    @Test
    void shouldReturnErrorWhenCannotFindInterviewScheduleToUpdate() throws Exception {
        doThrow(new ResourceNotFoundException("Interview with id: " + hrId + " not found")).when(interviewScheduleService).updateInterviewSchedule(any());
        this.mvc.perform(put("/api/interviews/{id}", hrId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(technicalInterviewDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Interview with id: " + hrId + " not found"));
    }

    @Test
    void shouldAddInterviewFeedback() throws Exception {
        given(interviewScheduleService.addTechInterviewFeedback(any())).willReturn(interviewFeedbackDTOAcceptedCandidate);

        mvc.perform(patch("/api/interviews/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewFeedbackDTOAcceptedCandidate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("6"))
                .andExpect(jsonPath("$.interviewId").value("1"))
                .andExpect(jsonPath("$.feedbackDate").value("2022-03-15T14:15:00"))
                .andExpect(jsonPath("$.candidateStatus").value("ACCEPTED"))
                .andExpect(jsonPath("$.proposedRole").value("Junior"));
    }

    @Test
    void shouldReturnBadRequestWhenInterviewFeedbackIsWrong() throws Exception {
        given(interviewScheduleService.addTechInterviewFeedback(any())).willThrow(RequestValidationException.class);
        mvc.perform(patch("/api/interviews/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(interviewFeedbackDTOAcceptedCandidate)))
                .andExpect(status().is4xxClientError());
    }
}
