package com.recruitment.mapper;

import com.recruitment.domain.InterviewSchedule;
import com.recruitment.dto.InterviewScheduleDTO;

import java.util.List;

public class InterviewScheduleMapper {
    public static InterviewSchedule buildEntity(InterviewScheduleDTO interviewScheduleDTO) {
        return InterviewSchedule.builder()
                .id(interviewScheduleDTO.getId())
                .startDate(interviewScheduleDTO.getStartDate())
                .endDate(interviewScheduleDTO.getEndDate())
                .candidateId(interviewScheduleDTO.getCandidateId())
                .appliedDepartmentId(interviewScheduleDTO.getAppliedDepartmentId())
                .interviewType(interviewScheduleDTO.getInterviewType())
                .build();
    }

    public static InterviewScheduleDTO buildDTO(InterviewSchedule interviewSchedule) {
        return InterviewScheduleDTO.builder()
                .id(interviewSchedule.getId())
                .startDate(interviewSchedule.getStartDate())
                .endDate(interviewSchedule.getEndDate())
                .candidateId(interviewSchedule.getCandidateId())
                .appliedDepartmentId(interviewSchedule.getAppliedDepartmentId())
                .interviewType(interviewSchedule.getInterviewType())
                .build();
    }

    public static InterviewScheduleDTO buildReadDTO(InterviewSchedule interviewSchedule, List<String> usersNames) {
        return InterviewScheduleDTO.builder()
                .id(interviewSchedule.getId())
                .startDate(interviewSchedule.getStartDate())
                .endDate(interviewSchedule.getEndDate())
                .candidateId(interviewSchedule.getCandidateId())
                .appliedDepartmentId(interviewSchedule.getAppliedDepartmentId())
                .interviewType(interviewSchedule.getInterviewType())
                .userNames(usersNames)
                .build();
    }
}
