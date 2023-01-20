package com.recruitment.controller;

import com.recruitment.dto.CommentDTO;
import com.recruitment.exception.handler.ErrorResponse;
import com.recruitment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Operation(summary = "Create comment", responses = {
            @ApiResponse(description = "Created comment", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(description = "Comment already exists", responseCode = "409",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public CommentDTO createComment(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Comment structure")
                                    @RequestBody CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }

    @Operation(summary = "Get all comments", responses = {
            @ApiResponse(description = "Received all comments", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @Operation(summary = "Get comment", responses = {
            @ApiResponse(description = "Found candidate", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(description = "Comment not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public CommentDTO getCommentById(@Parameter(description = "ID of the comment we want to get")
                                     @PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @Operation(summary = "Update comment", responses = {
            @ApiResponse(description = "Updated comment", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(description = "Comment not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public CommentDTO updateComment(@Parameter(description = "ID of the comment we want to update")
                                    @PathVariable Long id,
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Comment structure")
                                    @RequestBody CommentDTO commentDTO) {
        commentDTO.setId(id);
        return commentService.updateComment(commentDTO);
    }

    @Operation(summary = "Delete comment", responses = {
            @ApiResponse(description = "Deleted comment", responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(description = "Comment not found", responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(description = "Internal Server error", responseCode = "500",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public void deleteComment(@Parameter(description = "ID of the comment we want to delete")
                              @PathVariable Long id) {
        commentService.deleteComment(id);
    }
}

