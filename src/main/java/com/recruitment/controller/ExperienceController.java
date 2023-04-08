package com.recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.DocumentDTO;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    @PostMapping
    public ExperienceDTO createExperience(@RequestBody ExperienceDTO experience) {
        return experienceService.createExperience(experience);
    }

    @GetMapping
    public List<ExperienceDTO> getAllExperiences() {
        return experienceService.getAllExperiences();
    }


    @PatchMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ExperienceDTO uploadExperience(@ModelAttribute("experience") String experience,
                                          @RequestParam("file") MultipartFile document,
                                          @RequestParam("docType") DocumentType docType) {
        ObjectMapper mapper = new ObjectMapper();
        ExperienceDTO experienceDTO = null;
        try {
            experienceDTO = mapper.readValue(experience, ExperienceDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return experienceService.uploadExperience(experienceDTO, document, docType);
    }

    @GetMapping("/download/{candidateId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long candidateId, @RequestParam("docType") DocumentType docType) {
        DocumentDTO document = experienceService.getDocumentDetails(candidateId, docType);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + document.getDocumentName() + "\"")
                .body(document.getDocument());
    }
}
