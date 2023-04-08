package com.recruitment.service;

import com.recruitment.base.ExperienceBaseTest;
import com.recruitment.domain.Experience;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.DocumentDTO;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.exception.DocumentInvalidException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.repository.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class ExperienceServiceUnitTest extends ExperienceBaseTest {

    @Mock
    ExperienceRepository experienceRepository;

    @InjectMocks
    ExperienceService experienceService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldUploadExperience() {
        // given
        when(experienceRepository.save(experienceId1)).thenReturn(experienceId1);

        // when
        ExperienceDTO resultedExperienceDTO = experienceService.createExperience(experienceDTO);

        // then
        assertEquals(experienceId1.getCvName(), resultedExperienceDTO.getCvName());
        assertEquals(experienceId1.getComments(), resultedExperienceDTO.getComments());
    }

    @Test
    void shouldThrowExceptionWhenDocumentFormatNotSupportedForUpload() {
        // given
        String documentName = "document1.txt";
        document = new MockMultipartFile(
                documentName,
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );

        // when
        DocumentInvalidException documentInvalidException = assertThrows(DocumentInvalidException.class, () ->
                experienceService.uploadExperience(experienceDTO, document, documentType));

        // then
        assertEquals("The document format is not supported!", documentInvalidException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDocumentsCandidateNotFoundForUpload() {
        // given
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.empty());

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                experienceService.uploadExperience(experienceDTO, document, documentType));

        // then
        assertEquals("User with id: " + EXPERIENCE_ID + " not found.", resourceNotFoundException.getMessage());
    }


    @Test
    void shouldReturnAllTheExperiences() {
        //given
        List<Experience> experiences = Collections.singletonList(experienceId1);
        when(experienceRepository.findAll()).thenReturn(experiences);

        //when
        List<ExperienceDTO> resultedExperienceList = experienceService.getAllExperiences();

        //then
        assertEquals(experiences.size(), resultedExperienceList.size());
        assertEquals(experiences.get(0).getCvName(), resultedExperienceList.get(0).getCvName());
        assertEquals(experiences.get(0).getComments(), resultedExperienceList.get(0).getComments());
    }

    @Test
    void shouldUpdateCv() {
        // given
        String documentName = "document1.pdf";
        document = new MockMultipartFile(
                documentName,
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );

        Experience updatedExperience = Experience.builder().cvName("document1.pdf").build();
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.of(experienceId1));
        when(experienceRepository.save(experienceId1)).thenReturn(updatedExperience);

        // when
        ExperienceDTO resultedExperienceDTO = experienceService.uploadExperience(experienceDTO, document, DocumentType.CV);

        // then
        assertEquals(documentName, resultedExperienceDTO.getCvName());
    }

    @Test
    void shouldUpdateGdpr() {
        // given
        String documentName = "document1.pdf";
        document = new MockMultipartFile(
                documentName,
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );

        Experience updatedExperience = Experience.builder().gdprName("document1.pdf").build();
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.of(experienceId1));
        when(experienceRepository.save(experienceId1)).thenReturn(updatedExperience);

        // when
        ExperienceDTO resultedExperienceDTO = experienceService.uploadExperience(experienceDTO, document, DocumentType.GDPR);

        // then
        assertEquals(documentName, resultedExperienceDTO.getGdprName());
    }

    @Test
    void shouldThrowExceptionWhenExperienceNotFoundForDownload() {
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.empty());
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                experienceService.getDocumentDetails(experienceDTO.getCandidate().getId(), documentType));
        assertEquals("Experience with id: " + EXPERIENCE_ID + " not found.", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldReturnCvForDownload() throws Exception {
        // given
        String documentName = "document1.pdf";
        document = new MockMultipartFile(
                documentName,
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );
        experienceId1.setCv(document.getBytes());
        when(experienceRepository.findById(1L)).thenReturn(Optional.ofNullable(experienceId1));

        // when
        DocumentDTO resultedDocumentDTO = experienceService.getDocumentDetails(1L, DocumentType.CV);

        // then
        assertEquals(experienceId1.getCvName(), resultedDocumentDTO.getDocumentName());
    }

    @Test
    void shouldReturnGdprForDownload() throws Exception {
        // given
        String documentName = "document1.pdf";
        document = new MockMultipartFile(
                documentName,
                documentName,
                "text/plain",
                "This is the file content".getBytes()
        );
        experienceId1.setGdprName(documentName);
        experienceId1.setGdpr(document.getBytes());
        when(experienceRepository.findById(1L)).thenReturn(Optional.ofNullable(experienceId1));

        // when
        DocumentDTO resultedDocumentDTO = experienceService.getDocumentDetails(1L, DocumentType.GDPR);

        // then
        assertEquals(experienceId1.getGdprName(), resultedDocumentDTO.getDocumentName());
    }

    @Test
    void shouldThrowExceptionWhenCvNotFoundForDownload() {
        // given
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.ofNullable(experienceId1));

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                experienceService.getDocumentDetails(experienceDTO.getCandidate().getId(), documentType));

        // then
        assertEquals("Experience with id: " + EXPERIENCE_ID + " does not have a cv document in the database",
                resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenGdprNotFoundForDownload() {
        // given
        DocumentType gdprType = DocumentType.GDPR;
        when(experienceRepository.findById(experienceDTO.getCandidate().getId())).thenReturn(Optional.ofNullable(experienceId1));

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                experienceService.getDocumentDetails(experienceDTO.getCandidate().getId(), gdprType));

        // then
        assertEquals("Experience with id: " + EXPERIENCE_ID + " does not have a gdpr document in the database",
                resourceNotFoundException.getMessage());
    }
}

