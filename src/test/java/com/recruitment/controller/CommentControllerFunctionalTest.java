package com.recruitment.controller;

import com.recruitment.base.CommentBaseTest;
import com.recruitment.domain.Comment;
import com.recruitment.dto.CommentDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CommentMapper;
import com.recruitment.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerFunctionalTest extends CommentBaseTest {

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldReturnCommentDtoIfItCreatesCommentSuccessfully() throws Exception {
        given(commentService.createComment(commentResponseDTO)).willReturn(commentResponseDTO);

        this.mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.candidateId").value("1"))
                .andExpect(jsonPath("$.comment").value("Good job with answers"))
                .andExpect(jsonPath("$.date").value("2020-03-15T14:15:10"));
    }

    @Test
    void shouldReturnErrorWhenDuplicateComment() throws Exception {
        given(commentService.createComment(any()))
                .willThrow(new EntityAlreadyExistsException("Comment already exists!"));

        this.mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(CommentMapper.buildDTO(commentId2))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Comment already exists!"));
    }

    @Test
    void shouldDeleteSuccessfully() throws Exception {
        this.mockMvc.perform(delete("/api/comments/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(commentService).deleteComment(1L);
    }

    @Test
    void shouldReturnStatusOkWhenGettingAllComments() throws Exception {
        List<Comment> commentList = new ArrayList<>();
        commentList.add(commentId2);
        commentList.add(commentId3);
        List<CommentDTO> commentDTOList = CommentMapper.buildDTOs(commentList);

        given(commentService.getAllComments()).willReturn(commentDTOList);

        this.mockMvc.perform(get("/api/comments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("2"));
    }

    @Test
    void shouldReturnStatusOkWhenGettingCommentId() throws Exception {
        given(commentService.getCommentById(2L)).willReturn(CommentMapper.buildDTO(commentId2));
        this.mockMvc.perform(get("/api/comments/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.candidateId").value("1"))
                .andExpect(jsonPath("$.comment").value("Good job with answers"));
    }

    @Test
    void shouldReturnStatusOkWhenUpdatingExistingComment() throws Exception {
        CommentDTO commentDto = CommentDTO.builder()
                .id(1L)
                .userId(3L)
                .candidateId(1L)
                .comment("Good job with answers")
                .date(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        CommentDTO commentResponseDTO = CommentDTO.builder()
                .id(1L)
                .userId(3L)
                .candidateId(2L)
                .comment("Good job with answers1")
                .date(LocalDateTime.parse("2021-03-15T14:15:10"))
                .build();

        given(commentService.updateComment(commentDto)).willReturn(commentResponseDTO);

        this.mockMvc.perform(put("/api/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(commentDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value("3"))
                .andExpect(jsonPath("$.candidateId").value("2"))
                .andExpect(jsonPath("$.comment").value("Good job with answers1"))
                .andExpect(jsonPath("$.date").value("2021-03-15T14:15:10"));
    }

    @Test
    void shouldReturnErrorWhenCannotFindCommentToUpdate() throws Exception {
        doThrow(new ResourceNotFoundException("Candidate with id: 999 not found.")).when(commentService).updateComment(any());
        this.mockMvc.perform(put("/api/comments/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapperBuilder.build().writeValueAsString(commentDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Candidate with id: 999 not found."));
    }
}

