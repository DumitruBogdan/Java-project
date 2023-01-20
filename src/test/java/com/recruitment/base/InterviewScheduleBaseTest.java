package com.recruitment.base;

import com.recruitment.domain.InterviewFeedback;
import com.recruitment.domain.InterviewSchedule;
import com.recruitment.domain.User;
import com.recruitment.domain.enums.CandidateStatus;
import com.recruitment.domain.enums.InterviewType;
import com.recruitment.domain.enums.Role;
import com.recruitment.dto.InterviewFeedbackDTO;
import com.recruitment.dto.InterviewScheduleDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterviewScheduleBaseTest {

    protected Long hrId;
    protected Long pteId;
    protected InterviewSchedule hrInterview;
    protected InterviewScheduleDTO hrInterviewDTO;

    protected Long technicalUserId;
    protected InterviewSchedule technicalInterview;
    protected InterviewSchedule technicalInterviewForException;
    protected InterviewSchedule updatedInterviewSchedule;
    protected InterviewScheduleDTO technicalInterviewDTO;
    protected InterviewFeedback interviewFeedbackAcceptedCandidate;

    protected InterviewFeedbackDTO interviewFeedbackDTOAcceptedCandidate;

    protected InterviewFeedback interviewFeedbackBeforeInterview;

    protected InterviewFeedbackDTO interviewFeedbackDTOBeforeInterview;

    protected User pte;
    protected User hr;
    protected User technicalInterviewer;
    protected User admin;

    protected List<User> usersForInterview;
    protected List<Long> usersId;

    public void init() {
        pteId = 1L;
        hrId = 8L;

        pte = new User(1L, Role.PTE,"vasile", "popescu", "v.popescu@yahoo.com",
                1, "password", null);

        hr = new User(8L, Role.HR_REPRESENTATIVE, "mateiu", "ana",
                "mateiu.ana@yahoo.com", 1, "password", null);

        technicalInterviewer = new User(6L, Role.TECHNICAL_INTERVIEWER, "onciu", "ana",
                "oana@yahoo.com", 1, "password", null);

        admin = new User(3L, Role.ADMIN, "popescu", "Ion",
                "Dog@gmail.com", 1, "password", null);

        usersForInterview = new ArrayList<>();
        usersForInterview.add(hr);
        usersForInterview.add(hr);
        usersForInterview.add(pte);
        usersForInterview.add(technicalInterviewer);
        usersForInterview.add(technicalInterviewer);

        usersId = new ArrayList<>();
        usersId.add(hrId);
        usersId.add(hrId);
        usersId.add(pte.getId());
        usersId.add(technicalUserId);
        usersId.add(technicalUserId);

        hrInterview = new InterviewSchedule(hrId, LocalDateTime.parse("2022-03-15T14:15:00"),
                LocalDateTime.parse("2022-03-15T15:15:00"), 1L, 1,
                InterviewType.valueOf("HR"), usersForInterview);

        hrInterviewDTO = InterviewScheduleDTO.builder()
                .startDate(LocalDateTime.parse("2022-03-15T14:15:00"))
                .endDate(LocalDateTime.parse("2022-12-15T16:16:00"))
                .candidateId(1L)
                .appliedDepartmentId(1)
                .interviewType(InterviewType.valueOf("HR"))
                .userIds(usersId)
                .build();

        technicalUserId = 2L;

        technicalInterview = new InterviewSchedule(technicalUserId, LocalDateTime.parse("2022-05-17T11:00:00"),
                LocalDateTime.parse("2022-05-17T12:00:00"), 3L, 2,
                InterviewType.valueOf("TECHNICAL"), usersForInterview);

        updatedInterviewSchedule = new InterviewSchedule(technicalUserId, LocalDateTime.parse("2022-05-17T11:00:00"),
                LocalDateTime.parse("2022-05-17T12:00:00"), 3L, 2,
                InterviewType.valueOf("HR"), null);

        technicalInterviewDTO = InterviewScheduleDTO.builder()
                .id(2L)
                .startDate(LocalDateTime.parse("2022-05-17T11:00:00"))
                .endDate(LocalDateTime.parse("2022-05-17T12:00:00"))
                .candidateId(3L)
                .appliedDepartmentId(2)
                .interviewType(InterviewType.valueOf("TECHNICAL"))
                .userIds(usersId)
                .build();

        interviewFeedbackAcceptedCandidate = new InterviewFeedback(1L, 6L, 1L,
                LocalDateTime.parse("2022-03-15T14:15:00"), CandidateStatus.ACCEPTED, "Junior");

        interviewFeedbackDTOAcceptedCandidate = InterviewFeedbackDTO.builder()
                .id(1L)
                .userId(6L)
                .interviewId(1L)
                .feedbackDate(LocalDateTime.parse("2022-03-15T14:15:00"))
                .candidateStatus(CandidateStatus.ACCEPTED)
                .proposedRole("Junior")
                .build();

        interviewFeedbackBeforeInterview = new InterviewFeedback(1L, 6L, 1L,
                LocalDateTime.parse("1900-03-15T14:15:00"), CandidateStatus.NOT_ACCEPTED, "Junior");

        interviewFeedbackDTOBeforeInterview = InterviewFeedbackDTO.builder()
                .id(1L)
                .userId(6L)
                .interviewId(1L)
                .feedbackDate(LocalDateTime.parse("1900-03-15T14:15:00"))
                .candidateStatus(CandidateStatus.NOT_ACCEPTED)
                .proposedRole("Junior")
                .build();

        technicalInterviewForException = new InterviewSchedule(1L, LocalDateTime.parse("2022-03-15T14:15:00"),
                LocalDateTime.parse("2020-03-15T15:15:00"), 1L, 1,
                InterviewType.valueOf("TECHNICAL"), null);
    }
}
