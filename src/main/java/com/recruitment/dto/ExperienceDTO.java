package com.recruitment.dto;

import com.recruitment.domain.Candidate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {
    private Candidate candidate;
    @NotNull
    private String cvName;
    @NotNull
    private String gdprName;
    private String comments;
}
