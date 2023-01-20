package com.recruitment.mapper;

import com.recruitment.domain.InterviewFeedback;
import com.recruitment.dto.InterviewFeedbackDTO;

public class InterviewFeedbackMapper {
    public static InterviewFeedback buildEntity(InterviewFeedbackDTO interviewFeedbackDTO) {
        return InterviewFeedback.builder()
                .id(interviewFeedbackDTO.getId())
                .userId(interviewFeedbackDTO.getUserId())
                .interviewId(interviewFeedbackDTO.getInterviewId())
                .feedbackDate(interviewFeedbackDTO.getFeedbackDate())
                .candidateStatus(interviewFeedbackDTO.getCandidateStatus())
                .proposedRole(interviewFeedbackDTO.getProposedRole())
                .build();
    }

    public static InterviewFeedbackDTO buildDTO(InterviewFeedback interviewFeedback) {
        return InterviewFeedbackDTO.builder()
                .id(interviewFeedback.getId())
                .userId(interviewFeedback.getUserId())
                .interviewId(interviewFeedback.getInterviewId())
                .feedbackDate(interviewFeedback.getFeedbackDate())
                .candidateStatus(interviewFeedback.getCandidateStatus())
                .proposedRole(interviewFeedback.getProposedRole())
                .build();
    }
}
