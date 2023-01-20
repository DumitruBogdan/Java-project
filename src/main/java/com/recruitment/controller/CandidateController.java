package com.recruitment.controller;

import com.recruitment.dto.CandidateDTO;
import com.recruitment.dto.SearchDTO;
import com.recruitment.exception.handler.ErrorResponse;
import com.recruitment.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Tag(name = "Candidate")
@RequestMapping("/api/candidates")
public class CandidateController {
    @Autowired
    CandidateService candidateService;

    @Operation(summary = "Create candidate", responses = {
            @ApiResponse(description = "Created candidate", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Candidate already exists", responseCode = "409",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public CandidateDTO createCandidate(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Candidate structure")
                                        @RequestBody CandidateDTO candidateDTO) {
        return candidateService.createCandidate(candidateDTO);
    }

    @Operation(summary = "Delete candidate", responses = {
            @ApiResponse(description = "Deleted candidate", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Candidate not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteCandidate(@Parameter(description = "ID of the candidate we want to delete")
                                @PathVariable Long id) {
        candidateService.deleteCandidate(id);
    }

    @Operation(summary = "Get all candidates", responses = {
            @ApiResponse(description = "Received all candidates", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<CandidateDTO> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @Operation(summary = "Get candidate", responses = {
            @ApiResponse(description = "Found candidate", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Candidate not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public CandidateDTO getCandidateById(@Parameter(description = "ID of the candidate we want to get")
                                         @PathVariable Long id) {
        return candidateService.getCandidateById(id);
    }

    @Operation(summary = "Update candidate", responses = {
            @ApiResponse(description = "Updated candidate", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Candidate not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public CandidateDTO updateCandidate(@Parameter(description = "ID of the candidate we want to update")
                                        @PathVariable Long id,
                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Candidate structure")
                                        @RequestBody CandidateDTO candidateDTO) {
        candidateDTO.setId(id);
        return candidateService.updateCandidate(candidateDTO);
    }

    @Operation(summary = "Search candidates", responses = {
            @ApiResponse(description = "Candidates found", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CandidateDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })

    @GetMapping("/filter")
    public List<CandidateDTO> getFilteredCandidates(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Search structure")
                                                    @RequestBody SearchDTO searchDTO) {
        return candidateService.getFilteredCandidates(searchDTO);
    }
}
