package com.recruitment.domain;

import com.recruitment.domain.enums.CandidateStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interview_feedback")
public class InterviewFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long interviewId;

    private LocalDateTime feedbackDate;

    @Enumerated(EnumType.STRING)
    private CandidateStatus candidateStatus;

    private String proposedRole;
}
