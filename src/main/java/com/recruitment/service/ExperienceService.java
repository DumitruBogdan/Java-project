package com.recruitment.service;

import com.recruitment.domain.Experience;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.DocumentDTO;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.exception.DocumentInvalidException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.ExperienceMapper;
import com.recruitment.repository.ExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ExperienceService {

    private static final String SUPPORTED_DOCUMENTS = "doc|docx|pdf";
    private static final String REGEX_DOCUMENT_VALIDATION = "^.*\\.(" + SUPPORTED_DOCUMENTS + ")$";
    @Autowired
    ExperienceRepository experienceRepository;

    public ExperienceDTO createExperience(ExperienceDTO experience) {
        Experience savedExperience = experienceRepository.save(ExperienceMapper.buildEntity(experience));
        log.info("Experience saved for candidate with id: {}", experience.getCandidate().getId());
        return ExperienceMapper.buildDTO(savedExperience);
    }

    public List<ExperienceDTO> getAllExperiences() {
        return ExperienceMapper.buildDTOs(experienceRepository.findAll());
    }

    public ExperienceDTO uploadExperience(ExperienceDTO experienceDTO, MultipartFile document, DocumentType docType) {
        Experience updatedExperience;
        if (isDocumentValid(document)) {
            updatedExperience = saveUpdatedExperience(experienceDTO, document, docType);
//            log.info("Saved updated experience for candidate with id: {}", experienceDTO.getCandidateId());

        } else {
            log.error("The document format is not supported! Please insert a pdf, doc or docx document.");
            throw new DocumentInvalidException("The document format is not supported!");
        }
        return ExperienceMapper.buildDTO(updatedExperience);
    }

    private boolean isDocumentValid(MultipartFile document) {
        String fileName = document.getOriginalFilename();
        Pattern pattern = Pattern.compile(REGEX_DOCUMENT_VALIDATION, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileName);
        boolean isValid = matcher.find();
        log.debug("Document {} validity: {}", fileName, isValid);
        return isValid;
    }

    private Experience saveUpdatedExperience(ExperienceDTO experienceDTO, MultipartFile document, DocumentType docType) {
        Optional<Experience> dbExperience =null;// experienceRepository.findById(experienceDTO.getCandidateId());
        if (dbExperience.isEmpty()) {
//            log.error("User with id: {} not found. Can't fulfill update operation.", experienceDTO.getCandidateId());
//            throw new ResourceNotFoundException("User with id: " + experienceDTO.getCandidateId() + " not found.");
        }
        Experience experienceUpdated = dbExperience.get();
        try {
            if (docType.equals(DocumentType.CV)) {
                experienceUpdated.setCv(document.getBytes());
                experienceUpdated.setCvName(document.getOriginalFilename());
            }
            if (docType.equals(DocumentType.GDPR)) {
                experienceUpdated.setGdpr(document.getBytes());
                experienceUpdated.setGdprName(document.getOriginalFilename());
            }
            experienceUpdated.setComments(experienceDTO.getComments());
        } catch (IOException ioException) {
//            log.error("The file could not be read for experience with id: {}.", experienceDTO.getCandidateId());
            throw new DocumentInvalidException("The file could not be read.");
        }
        return experienceRepository.save(experienceUpdated);
    }

    public DocumentDTO getDocumentDetails(Long id, DocumentType docType) {
        Optional<Experience> dbExperience = experienceRepository.findById(id);
        DocumentDTO document = null;
        if (dbExperience.isPresent()) {
            if (docType.equals(DocumentType.CV)) {
                if (!StringUtils.hasText(dbExperience.get().getCvName()) || dbExperience.get().getCv() == null) {
                    log.error("Experience with id: {} does not have a cv in database.", id);
                    throw new ResourceNotFoundException("Experience with id: " + id + " does not have a cv document in the database");
                } else {
                    document = new DocumentDTO(dbExperience.get().getCvName(), dbExperience.get().getCv());
                }
            }
            if (docType.equals(DocumentType.GDPR)) {
                if (!StringUtils.hasText(dbExperience.get().getGdprName()) || dbExperience.get().getGdpr() == null) {
                    log.error("Experience with id: {} does not have a gdpr in database.", id);
                    throw new ResourceNotFoundException("Experience with id: " + id + " does not have a gdpr document in the database");
                } else {
                    document = new DocumentDTO(dbExperience.get().getGdprName(), dbExperience.get().getGdpr());
                }
            }
        } else {
            log.error("Experience with id: {} not found. Can't fulfill download document operation.", id);
            throw new ResourceNotFoundException("Experience with id: " + id + " not found.");
        }
        return document;
    }

    public ExperienceDTO getByCandidateId(Long id) {
        return ExperienceMapper.buildDTO(experienceRepository.getById(id));
    }
}