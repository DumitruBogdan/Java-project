package com.recruitment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.domain.enums.DocumentType;
import com.recruitment.dto.DocumentDTO;
import com.recruitment.dto.ExperienceDTO;
import com.recruitment.exception.handler.ErrorResponse;
import com.recruitment.service.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Experience")
@RequestMapping("/api/experiences")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    @Operation(summary = "Create experience", responses = {
            @ApiResponse(description = "Created experience", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ExperienceDTO createExperience(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Experience structure")
                                          @RequestBody ExperienceDTO experience) {
        return experienceService.createExperience(experience);
    }

    @Operation(summary = "Get all experiences", responses = {
            @ApiResponse(description = "Received all experiences", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<ExperienceDTO> getAllExperiences() {
        return experienceService.getAllExperiences();
    }

    @Operation(summary = "Upload experience", responses = {
            @ApiResponse(description = "Uploaded experience", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ExperienceDTO uploadExperience(@Parameter(description = "Experience schema")
                                          @ModelAttribute("experience") String experience,
                                          @Parameter(description = "Document we want to upload")
                                          @RequestParam("file") MultipartFile document,
                                          @Parameter(description = "Type of document we want to upload")
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

    @Operation(summary = "Download experience", responses = {
            @ApiResponse(description = "Downloaded experience", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExperienceDTO.class))),
            @ApiResponse(description = "Experience not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/download/{candidateId}")
    public ResponseEntity<byte[]> downloadFile(@Parameter(description = "ID of the file we want to download")
                                               @PathVariable Long candidateId,
                                               @Parameter(description = "The type of document we want to download")
                                               @RequestParam("docType") DocumentType docType) {
        DocumentDTO document = experienceService.getDocumentDetails(candidateId, docType);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + document.getDocumentName() + "\"")
                .body(document.getDocument());
    }
}
