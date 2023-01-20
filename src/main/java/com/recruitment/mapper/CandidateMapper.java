package com.recruitment.mapper;

import com.recruitment.domain.Candidate;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.dto.CandidateDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateMapper {
    public static Candidate buildEntity(CandidateDTO candidateDTO) {
        return Candidate.builder()
                .id(candidateDTO.getId())
                .firstName(candidateDTO.getFirstName())
                .lastName(candidateDTO.getLastName())
                .gender(candidateDTO.getGender())
                .email(candidateDTO.getEmail())
                .phoneNumber(candidateDTO.getPhoneNumber())
                .country(candidateDTO.getCountry())
                .address(candidateDTO.getAddress())
                .username(candidateDTO.getUsername())
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(candidateDTO.getLastLoginDate())
                .password(candidateDTO.getPassword())
                .hiredStatus(candidateDTO.getHiredStatus())
                .build();
    }

    public static CandidateDTO buildDTO(Candidate candidate) {
        return CandidateDTO.builder()
                .id(candidate.getId())
                .firstName(candidate.getFirstName())
                .lastName(candidate.getLastName())
                .gender(candidate.getGender())
                .email(candidate.getEmail())
                .phoneNumber(candidate.getPhoneNumber())
                .country(candidate.getCountry())
                .address(candidate.getAddress())
                .username(candidate.getUsername())
                .accountStatus(candidate.getAccountStatus())
                .lastLoginDate(candidate.getLastLoginDate())
                .hiredStatus(candidate.getHiredStatus())
                .build();
    }

    public static List<CandidateDTO> buildDTOs(List<Candidate> candidates) {
        return candidates.stream()
                .map(CandidateMapper::buildDTO)
                .collect(Collectors.toList());
    }
}
