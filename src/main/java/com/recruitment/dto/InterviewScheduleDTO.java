package com.recruitment.dto;

import com.recruitment.domain.enums.InterviewType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class InterviewScheduleDTO {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long candidateId;
    private int appliedDepartmentId;
    @NotNull
    private InterviewType interviewType;
    @NotNull
    private List<String> userNames;
    @NotNull
    private List<Long> userIds;
}
