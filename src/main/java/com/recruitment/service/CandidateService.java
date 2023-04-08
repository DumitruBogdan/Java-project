package com.recruitment.service;

import com.recruitment.domain.Candidate;
import com.recruitment.domain.Experience;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.dto.CandidateDTO;
import com.recruitment.dto.SearchDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CandidateMapper;
import com.recruitment.repository.CandidateRepository;
import com.recruitment.repository.ExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CandidateService {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    ExperienceRepository experienceRepository;

    @Transactional
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        System.out.println(candidateDTO);
        if (candidateRepository.existsByUsername(candidateDTO.getUsername())) {
            log.error("Create failed for username {} being duplicate.", candidateDTO.getUsername());
            throw new EntityAlreadyExistsException("User already exists!");
        }
        Candidate createdCandidate = candidateRepository.save(CandidateMapper.buildEntity(candidateDTO));
        experienceRepository.save(Experience.builder().candidate(createdCandidate).build());
        log.info("Candidate with id: {} was created with success.", createdCandidate.getId());
        return CandidateMapper.buildDTO(createdCandidate);
    }

    @Transactional
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            log.warn("No candidate with id: {} exist in database", id);
            throw new ResourceNotFoundException("Candidate does not exist!");
        }
        candidateRepository.deleteById(id);
        log.info("Candidate with id: {} was deleted with success.", id);
    }

    public List<CandidateDTO> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        log.debug("Current number of candidates in database: {}", candidates.size());
        if (candidates.isEmpty()) {
            log.warn("No candidates exist in database.");
            throw new ResourceNotFoundException("Candidates list is empty");
        }
        return CandidateMapper.buildDTOs(candidates);
    }

    public CandidateDTO getCandidateById(Long id) {
        if (id == null) {
            log.error("Get method failed for id being null");
            throw new ResourceNotFoundException("Candidate ID is missing");
        }
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isEmpty()) {
            log.error("Could not find candidate with id: {}", id);
            throw new ResourceNotFoundException("Candidate with id: " + id + " not found.");
        }
        return CandidateMapper.buildDTO(candidate.get());
    }

    public CandidateDTO updateCandidate(CandidateDTO candidateDTO) {
        Optional<Candidate> dbCandidate = candidateRepository.findById(candidateDTO.getId());
        if (dbCandidate.isEmpty()) {
            log.error("Candidate with id: {} not found. Can't fulfill update operation.", candidateDTO.getId());
            throw new ResourceNotFoundException("Candidate with id: " + candidateDTO.getId() + " not found.");
        }
        Candidate updatedCandidate = candidateRepository.save(CandidateMapper.buildEntity(candidateDTO));
        log.info("Candidate with id: {} was updated with success.", updatedCandidate.getId());
        return CandidateMapper.buildDTO(updatedCandidate);
    }


    public List<CandidateDTO> getFilteredCandidates(SearchDTO searchDTO) {
        List<String> options = searchDTO.getItems();
        String filteredBy = searchDTO.getColumnName();
        List<Candidate> candidates;
        if (!StringUtils.hasText(filteredBy)) {
            if (options == null || options.isEmpty()) {
                candidates = candidateRepository.findAll();
            } else {
                candidates = candidateRepository.findByKeyword(options.get(0));
            }
        } else {
            switch (filteredBy) {
                case "accountStatus" -> candidates = filterByAccountStatus(options);
                case "date" -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    candidates = candidateRepository.findAllByLastLoginDateLessThanEqualAndLastLoginDateGreaterThanEqual(
                            LocalDateTime.parse(options.get(1), formatter), LocalDateTime.parse(options.get(0), formatter));
                }
                default -> candidates = candidateRepository.findByColumnAndKeyword(filteredBy, options.get(0));
            }
        }
        return buildResponse(candidates);
    }

    private List<Candidate> filterByAccountStatus(List<String> options) {
        List<AccountStatus> accountStatuses = new ArrayList<>();
        for (String option : options) {
            Optional<AccountStatus> accountStatus = AccountStatus.get(option);
            accountStatus.ifPresent(accountStatuses::add);
        }
        return candidateRepository.findByAccountStatusIn(accountStatuses);
    }

    public List<CandidateDTO> buildResponse(List<Candidate> candidates) {
        if (candidates.isEmpty()) {
            log.debug("No candidates exist in database. Check your filters.");
        }
        return CandidateMapper.buildDTOs(candidates);
    }
}
