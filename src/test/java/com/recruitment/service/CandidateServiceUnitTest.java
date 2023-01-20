package com.recruitment.service;

import com.recruitment.base.CandidateBaseTest;
import com.recruitment.domain.Candidate;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.dto.CandidateDTO;
import com.recruitment.dto.SearchDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CandidateMapper;
import com.recruitment.repository.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateServiceUnitTest extends CandidateBaseTest {

    @Mock
    CandidateRepository candidateRepository;

    @InjectMocks
    CandidateService candidateService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldThrowExceptionWhenCandidateExistsByUsername() {
        CandidateDTO updatedCandidate = candidateDTO;

        when(candidateRepository.existsByUsername(candidateDTO.getUsername())).thenReturn(true);
        EntityAlreadyExistsException entityAlreadyExistsException = assertThrows(EntityAlreadyExistsException.class, () ->
                candidateService.createCandidate(updatedCandidate));

        assertEquals("User already exists!", entityAlreadyExistsException.getMessage());
    }

    @Test
    void shouldCreateCandidateAndReturnCandidateDTO() {
        // given
        when(candidateRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        // when
        CandidateDTO result = candidateService.createCandidate(candidateId2DTO);

        // then
        assertEquals(2L, result.getId());
        assertEquals("Ana", result.getFirstName());
        assertEquals("Popescu", result.getLastName());
        assertEquals("Female", result.getGender());
        assertEquals("ana.popescu@gmail.com", result.getEmail());
        assertEquals("998-253-8290", result.getPhoneNumber());
        assertEquals("Romania", result.getCountry());
        assertEquals("Alba Iulia, Strada Albastrelelor nr. 9", result.getAddress());
        assertNull(null, result.getPassword());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());
        assertEquals(LocalDateTime.parse("2020-03-15T14:15:00"), result.getLastLoginDate());
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFoundForDeleteCandidate() {
        // given
        when(candidateRepository.existsById(CANDIDATE_ID)).thenReturn(false);

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                candidateService.deleteCandidate(CANDIDATE_ID));

        // then
        assertEquals("Candidate does not exist!", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldDeleteCandidateById() {
        // given
        when(candidateRepository.existsById(CANDIDATE_ID)).thenReturn(true);

        // when
        candidateService.deleteCandidate(CANDIDATE_ID);

        // then
        verify(candidateRepository).deleteById(CANDIDATE_ID);
    }

    @Test
    void shouldThrowExceptionWhenCandidateListIsEmpty() {
        // given
        when(candidateRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                candidateService.getAllCandidates());

        // then
        assertEquals("Candidates list is empty", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldReturnAllListOfCandidates() {
        // given
        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(candidateId2);
        candidateList.add(candidateId3);

        when(candidateRepository.findAll()).thenReturn(candidateList);

        // when
        List<CandidateDTO> results = candidateService.getAllCandidates();
        List<CandidateDTO> expected = CandidateMapper.buildDTOs(candidateList);

        // then
        assertEquals(expected, results);
    }

    @Test
    void shouldThrowExceptionWhenCandidateIdIsNull() {
        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                candidateService.getCandidateById(null));

        // then
        assertEquals("Candidate ID is missing", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFoundForGetCandidateById() {
        // given
        when(candidateRepository.findById(CANDIDATE_ID)).thenReturn(Optional.empty());

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                candidateService.getCandidateById(CANDIDATE_ID));

        // then
        assertEquals("Candidate with id: " + CANDIDATE_ID + " not found.", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldReturnCandidateDTO() {
        //given
        when(candidateRepository.findById(CANDIDATE_ID)).thenReturn(Optional.of(candidateId2));

        //when
        CandidateDTO result = candidateService.getCandidateById(CANDIDATE_ID);

        //then
        assertEquals(2L, result.getId());
        assertEquals("Ana", result.getFirstName());
        assertEquals("Popescu", result.getLastName());
        assertEquals("Female", result.getGender());
        assertEquals("ana.popescu@gmail.com", result.getEmail());
        assertEquals("998-253-8290", result.getPhoneNumber());
        assertEquals("Romania", result.getCountry());
        assertEquals("Alba Iulia, Strada Albastrelelor nr. 9", result.getAddress());
        assertEquals("anapopescu", result.getUsername());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());
        assertEquals(LocalDateTime.parse("2022-04-15T14:15:00"), result.getLastLoginDate());
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFoundForUpdateCandidate() {
        //given
        when(candidateRepository.findById(candidateId2.getId())).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                candidateService.updateCandidate(candidateId2DTO));

        //then
        assertEquals("Candidate with id: " + CANDIDATE_ID + " not found.", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldUpdateCandidate() {
        // given
        when(candidateRepository.findById(2L)).thenReturn(Optional.of(CandidateMapper.buildEntity(candidateId2DTO)));
        when(candidateRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        // when
        CandidateDTO result = candidateService.updateCandidate(candidateId2DTO);

        // then
        assertEquals("Ana", result.getFirstName());
    }

    @Test
    void shouldReturnAllCandidatesWhenFiltersAreEmpty() {
        //given
        List<Candidate> candidates = Collections.singletonList(candidateId2);
        when(candidateRepository.findAll()).thenReturn(candidates);

        //when
        List<CandidateDTO> resultedCandidates = candidateService.getFilteredCandidates(SearchDTO.builder().build());

        //then
        assertEquals(candidates.size(), resultedCandidates.size());
        assertEquals(candidates.get(0).getUsername(), resultedCandidates.get(0).getUsername());
    }

    @Test
    void shouldReturnCandidatesFilteredByKeyword() {
        //given
        SearchDTO searchDTO = SearchDTO.builder().items(Collections.singletonList("Female")).build();
        List<Candidate> candidates = Collections.singletonList(candidateId2);
        when(candidateRepository.findByKeyword(searchDTO.getItems().get(0))).thenReturn(candidates);

        //when
        List<CandidateDTO> resultedCandidates = candidateService.getFilteredCandidates(searchDTO);

        //then
        assertEquals(candidates.size(), resultedCandidates.size());
        assertEquals(candidates.get(0).getGender(), resultedCandidates.get(0).getGender());
    }

    @Test
    void shouldReturnCandidatesFilteredByAccountStatus() {
        //given
        SearchDTO searchDTO = SearchDTO.builder()
                .items(Collections.singletonList("ACTIVE"))
                .columnName("accountStatus").build();
        List<Candidate> candidates = Collections.singletonList(candidateId2);
        List<AccountStatus> accountStatuses = Collections.singletonList(AccountStatus.ACTIVE);
        when(candidateRepository.findByAccountStatusIn(accountStatuses)).thenReturn(candidates);

        //when
        List<CandidateDTO> resultedCandidates = candidateService.getFilteredCandidates(searchDTO);

        //then
        assertEquals(candidates.size(), resultedCandidates.size());
        assertEquals(candidates.get(0).getAccountStatus(), resultedCandidates.get(0).getAccountStatus());
    }

    @Test
    void shouldReturnCandidatesFilteredByDate() {
        //given
        List<String> dateTimes = new ArrayList<>();
        dateTimes.add("2022-04-14 14:15:00");
        dateTimes.add("2022-04-16 14:15:00");
        SearchDTO searchDTO = SearchDTO.builder()
                .items(dateTimes)
                .columnName("date").build();
        List<Candidate> candidates = Collections.singletonList(candidateId2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        when(candidateRepository.findAllByLastLoginDateLessThanEqualAndLastLoginDateGreaterThanEqual(
                LocalDateTime.parse(searchDTO.getItems().get(1), formatter), LocalDateTime.parse(searchDTO.getItems().get(0),
                        formatter)))
                .thenReturn(candidates);

        //when
        List<CandidateDTO> resultedCandidates = candidateService.getFilteredCandidates(searchDTO);

        //then
        assertEquals(candidates.size(), resultedCandidates.size());
        assertEquals(candidates.get(0).getUsername(), resultedCandidates.get(0).getUsername());
    }

    @Test
    void shouldReturnCandidatesFilteredByColumnAndKeyword() {
        //given
        SearchDTO searchDTO = SearchDTO.builder()
                .items(Collections.singletonList("Ion"))
                .columnName("firstName").build();
        List<Candidate> candidates = Collections.singletonList(candidateId3);
        when(candidateRepository.findByColumnAndKeyword("firstName", "Ion")).thenReturn(candidates);

        //when
        List<CandidateDTO> resultedCandidates = candidateService.getFilteredCandidates(searchDTO);

        //then
        assertEquals(candidates.size(), resultedCandidates.size());
        assertEquals(candidates.get(0).getFirstName(), resultedCandidates.get(0).getFirstName());
    }
}
