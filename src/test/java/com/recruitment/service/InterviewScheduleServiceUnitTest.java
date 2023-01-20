package com.recruitment.service;

import com.recruitment.base.InterviewScheduleBaseTest;
import com.recruitment.domain.InterviewFeedback;
import com.recruitment.domain.InterviewSchedule;
import com.recruitment.domain.User;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.dto.CandidateDTO;
import com.recruitment.dto.InterviewScheduleDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.RequestValidationException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.InterviewFeedbackMapper;
import com.recruitment.mapper.InterviewScheduleMapper;
import com.recruitment.repository.InterviewFeedbackRepository;
import com.recruitment.repository.InterviewScheduleRepository;
import com.recruitment.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterviewScheduleServiceUnitTest extends InterviewScheduleBaseTest {
    @InjectMocks
    private InterviewScheduleService interviewScheduleService;

    @Mock
    private InterviewFeedbackRepository interviewFeedbackRepository;
    @Mock
    private InterviewScheduleRepository interviewScheduleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock CandidateService candidateService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldCreateInterviewScheduleForHrInterview() {
        //given
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
                .password("parola")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:00"))
                .build();
        when(candidateService.getCandidateById(1L)).thenReturn(candidateDTO);
        when(userRepository.findByIdIn(usersId)).thenReturn(hrInterview.getUsers());
        when(interviewScheduleRepository.save(any(InterviewSchedule.class))).thenReturn(hrInterview);

        // when
        InterviewScheduleDTO resultedInterviewDTO = interviewScheduleService.createInterview(hrInterviewDTO);

        // then
        Assertions.assertThat(resultedInterviewDTO.getId()).isEqualTo(hrInterview.getId());
    }

    @Test
    void shouldCreateInterviewScheduleForTechnicalInterview() {
        //given
        CandidateDTO candidateDTO = CandidateDTO.builder()
                .id(3L)
                .firstName("Ana")
                .lastName("Popescu")
                .gender("Female")
                .email("ana.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("anapopescu")
                .password("parola")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:00"))
                .build();
        when(candidateService.getCandidateById(3L)).thenReturn(candidateDTO);
        when(userRepository.findByIdIn(usersId)).thenReturn(technicalInterview.getUsers());
        when(interviewScheduleRepository.save(any(InterviewSchedule.class))).thenReturn(technicalInterview);

        // when
        InterviewScheduleDTO resultedInterviewDTO = interviewScheduleService.createInterview(technicalInterviewDTO);

        // then
        Assertions.assertThat(resultedInterviewDTO.getId()).isEqualTo(technicalInterview.getId());
    }

    @Test
    void shouldReturnExceptionWhenNumberOfHrIsLowerThanRequested() {
        //given
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
                .password("parola")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:00"))
                .build();
        List<Long> invalidIds = new ArrayList<>();
        invalidIds.add(pte.getId());
        invalidIds.add(hr.getId());
        invalidIds.add(technicalInterviewer.getId());
        List<User> invalidUsers = new ArrayList<>();
        invalidUsers.add(pte);
        invalidUsers.add(hr);
        invalidUsers.add(technicalInterviewer);
        hrInterviewDTO.setUserIds(invalidIds);
        hrInterview.setUsers(invalidUsers);
        when(candidateService.getCandidateById(1L)).thenReturn(candidateDTO);
        when(userRepository.findByIdIn(invalidIds)).thenReturn(hrInterview.getUsers());

        // when
        assertThatThrownBy(() -> interviewScheduleService.createInterview(hrInterviewDTO))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("The minimum number of HR recruiters is: 2, current number is: 1");
    }

    @Test
    void getInterviewById() {
        // given
        when(interviewScheduleRepository.getReferenceById(1L)).thenReturn(hrInterview);

        // when
        InterviewScheduleDTO interviewScheduleDTO = interviewScheduleService.getInterviewById(1L);

        // then
        Assertions.assertThat(interviewScheduleDTO.getId()).isEqualTo(hrInterview.getId());
    }

    @Test
    void shouldUpdateInterviewSchedule() {
        //given
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
                .password("parola")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:00"))
                .build();
        hrInterviewDTO.setId(1L);
        when(interviewScheduleRepository.findById(1L)).thenReturn(Optional.ofNullable(hrInterview));
        when(candidateService.getCandidateById(1L)).thenReturn(candidateDTO);
        when(userRepository.findByIdIn(usersId)).thenReturn(hrInterview.getUsers());
        when(interviewScheduleRepository.save(any(InterviewSchedule.class))).thenReturn(hrInterview);

        // when
        InterviewScheduleDTO resultedInterviewDTO = interviewScheduleService.updateInterviewSchedule(hrInterviewDTO);

        // then
        Assertions.assertThat(resultedInterviewDTO.getId()).isEqualTo(hrInterview.getId());
    }

    @Test
    void shouldCreateInterviewFeedback() {
        // given
        InterviewFeedback savedInterviewFeedback = InterviewFeedbackMapper.buildEntity(interviewFeedbackDTOAcceptedCandidate);
        when(interviewFeedbackRepository.save(any(InterviewFeedback.class))).thenReturn(savedInterviewFeedback);
        when(userRepository.findById(6L)).thenReturn(Optional.of(technicalInterviewer));
        when(interviewScheduleRepository.findById(1L)).thenReturn(Optional.of(technicalInterviewForException));

        // when
        interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOAcceptedCandidate);

        ArgumentCaptor<InterviewFeedback> interviewFeedbackCaptor = ArgumentCaptor.forClass(InterviewFeedback.class);
        verify(interviewFeedbackRepository).save(interviewFeedbackCaptor.capture());
        InterviewFeedback capturedInterviewFeedback = interviewFeedbackCaptor.getValue();

        // then
        Assertions.assertThat(capturedInterviewFeedback).isEqualTo(savedInterviewFeedback);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        assertThatThrownBy(() -> interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOAcceptedCandidate))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found!");
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotTechnicalInterviewer() {
        // given
        when(userRepository.findById(6L)).thenReturn(Optional.of(admin));

        // then
        assertThatThrownBy(() -> interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOAcceptedCandidate))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("The user is not a technical interviewer!");
    }

    @Test
    void shouldThrowExceptionWhenFeedbackIsBeforeInterview() {
        // given
        when(userRepository.findById(6L)).thenReturn(Optional.of(technicalInterviewer));
        when(interviewScheduleRepository.findById(1L)).thenReturn(Optional.of(technicalInterview));

        // then
        assertThatThrownBy(() -> interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOBeforeInterview))
                .isInstanceOf(RequestValidationException.class)
                .hasMessageContaining("The interview feedback was written before the interview was done!");
    }

    @Test
    void shouldThrowExceptionWhenInterviewIsNotFound() {
        // given
        when(userRepository.findById(6L)).thenReturn(Optional.of(technicalInterviewer));

        // then
        assertThatThrownBy(() -> interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOAcceptedCandidate))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("The interview does not exist!");
    }

    @Test
    void shouldThrowExceptionWhenInterviewFeedbackAlreadyExists() {
        // given
        when(userRepository.findById(6L)).thenReturn(Optional.of(technicalInterviewer));
        when(interviewScheduleRepository.findById(1L)).thenReturn(Optional.of(technicalInterviewForException));
        when(interviewFeedbackRepository.findByInterviewId(1L)).thenReturn(Collections.singletonList(interviewFeedbackAcceptedCandidate));

        // then
        assertThatThrownBy(() -> interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTOAcceptedCandidate))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("The user can only add one feedback message to an interview!");
    }

    @Test
    void shouldDeleteInterviewScheduleById() {
        // given
        given(interviewScheduleRepository.findById(hrId)).willReturn(Optional.of(hrInterview));

        // when
        interviewScheduleService.deleteInterviewScheduleById(hrId);

        // then
        verify(interviewScheduleRepository).deleteById(hrId);
    }

    @Test
    void shouldThrowExceptionWhenCannotFindInterviewScheduleToDelete() {
        // given
        given(interviewScheduleRepository.findById(hrId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> interviewScheduleService.deleteInterviewScheduleById(hrId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("The interview desired to be deleted do not exist");
    }

    @Test
    void shouldThrowExceptionWhenCannotFindInterviewScheduleToUpdate() {
        // given
        given(interviewScheduleRepository.findById(hrId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> interviewScheduleService.updateInterviewSchedule(InterviewScheduleMapper.buildDTO(hrInterview)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Interview with interviewId: " + hrId + "not found.");
    }
}
