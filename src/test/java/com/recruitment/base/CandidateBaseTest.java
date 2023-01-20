package com.recruitment.base;

import com.recruitment.domain.Candidate;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.dto.CandidateDTO;

import java.time.LocalDateTime;

public class CandidateBaseTest {

    protected final Long CANDIDATE_ID = 2L;
    protected Candidate candidateId2;
    protected Candidate candidateId3;
    protected CandidateDTO candidateDTO;
    protected CandidateDTO candidateResponseDTO;
    protected CandidateDTO candidateId2DTO;

    public void init() {

        candidateDTO = CandidateDTO.builder()
                .firstName("Ana")
                .lastName("Popescu")
                .gender("Female")
                .email("ana.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("anapopescu")
                .password("parola")
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        candidateResponseDTO = CandidateDTO.builder()
                .id(1L)
                .firstName("Ana")
                .lastName("Popescu")
                .gender("Female")
                .email("ana.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("anapopescu")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        candidateId2 = Candidate.builder()
                .id(2L)
                .firstName("Ana")
                .lastName("Popescu")
                .gender("Female")
                .email("ana.popescu@gmail.com")
                .phoneNumber("998-253-8290")
                .country("Romania")
                .address("Alba Iulia, Strada Albastrelelor nr. 9")
                .username("anapopescu")
                .accountStatus(AccountStatus.ACTIVE)
                .lastLoginDate(LocalDateTime.parse("2022-04-15T14:15:00"))
                .build();

        candidateId3 = Candidate.builder()
                .id(3L)
                .firstName("Ion")
                .lastName("Petrescu")
                .gender("Male")
                .email("ion.petrescu@gmail.com")
                .phoneNumber("111-111-1111")
                .country("Romania")
                .address("Alba Iulia, Strada Rozelor nr. 3")
                .username("ionpetrescu")
                .accountStatus(AccountStatus.ARCHIVED)
                .lastLoginDate(LocalDateTime.parse("2019-03-15T14:15:00"))
                .build();

        candidateId2DTO = CandidateDTO.builder()
                .id(2L)
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
    }
}
