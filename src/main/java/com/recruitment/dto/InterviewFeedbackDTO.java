package com.recruitment.dto;

import com.recruitment.domain.enums.CandidateStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class InterviewFeedbackDTO {
    private Long id;
    private Long userId;
    private Long interviewId;
    private LocalDateTime feedbackDate;
    private CandidateStatus candidateStatus;
    @NotNull
    private String proposedRole;
}
