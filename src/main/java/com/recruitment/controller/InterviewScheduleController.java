package com.recruitment.controller;

import com.recruitment.dto.InterviewFeedbackDTO;
import com.recruitment.dto.InterviewScheduleDTO;
import com.recruitment.exception.handler.ErrorResponse;
import com.recruitment.service.InterviewScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interviews")
public class InterviewScheduleController {
    @Autowired
    InterviewScheduleService interviewScheduleService;

    @Operation(summary = "Create interview", responses = {
            @ApiResponse(description = "Created interview", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewScheduleDTO.class))),
            @ApiResponse(description = "One or more users not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public InterviewScheduleDTO createInterview(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Interview structure")
                                                @RequestBody InterviewScheduleDTO interviewScheduleDTO) {
        return interviewScheduleService.createInterview(interviewScheduleDTO);
    }

    @Operation(summary = "Read interview", responses = {
            @ApiResponse(description = "Read interview", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewScheduleDTO.class))),
            @ApiResponse(description = "Interview not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public InterviewScheduleDTO readInterviewById(@Parameter(description = "ID of the interview we want to read")
                                                  @PathVariable Long id) {
        return interviewScheduleService.getInterviewById(id);
    }

    @Operation(summary = "Delete interview by id", responses = {
            @ApiResponse(description = "Deleted interview", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewScheduleDTO.class))),
            @ApiResponse(description = "Interview not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteInterviewScheduleById(@Parameter(description = "Id of the interview we want to delete")
                                            @PathVariable("id") Long interviewId) {
        interviewScheduleService.deleteInterviewScheduleById(interviewId);
    }

    @Operation(summary = "Update interview", responses = {
            @ApiResponse(description = "Updated interview", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewScheduleDTO.class))),
            @ApiResponse(description = "Interview not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public InterviewScheduleDTO updateInterviewScheduleById(@Parameter(description = "Interview id")
                                                            @PathVariable("id") Long interviewId,
                                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Interview structure")
                                                            @RequestBody InterviewScheduleDTO updateInterviewSchedule) {
        updateInterviewSchedule.setId(interviewId);
        return interviewScheduleService.updateInterviewSchedule(updateInterviewSchedule);
    }

    @Operation(summary = "Add interview tech feedback", responses = {
            @ApiResponse(description = "Added interview feedback", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InterviewFeedbackDTO.class))),
            @ApiResponse(description = "Interview not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/feedback")
    public InterviewFeedbackDTO addTechInterviewFeedback(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Interview feedback structure")
                                                         @RequestBody InterviewFeedbackDTO interviewFeedbackDTO) {
        return interviewScheduleService.addTechInterviewFeedback(interviewFeedbackDTO);
    }
}
