package com.recruitment.mapper;

import com.recruitment.domain.Experience;
import com.recruitment.dto.ExperienceDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ExperienceMapper {
    public static Experience buildEntity(ExperienceDTO experienceDTO) {
        return Experience.builder()
                .candidate(experienceDTO.getCandidate())
                .cvName(experienceDTO.getCvName())
                .gdprName(experienceDTO.getGdprName())
                .comments(experienceDTO.getComments())
                .build();
    }

    public static ExperienceDTO buildDTO(Experience experience) {
        return ExperienceDTO.builder()
                .candidate(experience.getCandidate())
                .cvName(experience.getCvName())
                .gdprName(experience.getGdprName())
                .comments(experience.getComments())
                .build();
    }

    public static List<ExperienceDTO> buildDTOs(List<Experience> experiences) {
        return experiences.stream()
                .map(ExperienceMapper::buildDTO)
                .collect(Collectors.toList());
    }
}
