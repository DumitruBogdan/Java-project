package com.recruitment.service;

import com.recruitment.base.CommentBaseTest;
import com.recruitment.domain.Comment;
import com.recruitment.dto.CommentDTO;
import com.recruitment.exception.EntityNotFoundException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CommentMapper;
import com.recruitment.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceUnitTest extends CommentBaseTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @BeforeEach
    void setup() {
        init();
    }

    @Test
    void shouldCreateCommentAndReturnCommentDTO() {
        //given
        when(commentRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        // when
        CommentDTO result = commentService.createComment(commentIdDTO);

        // then
        assertEquals(4L, result.getCandidateId());
        assertEquals(5L, result.getUserId());
        assertEquals("Good job with answers", result.getComment());
    }

    @Test
    void shouldThrowExceptionWhenCandidateIdIsNullForAddingComment() {
        //given
        CommentDTO commentDTO = CommentDTO.builder().build();

        //when
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () ->
                commentService.createComment(commentDTO));

        //then
        assertEquals("Content of comment does not exists!", entityNotFoundException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFoundForDeleteComment() {
        // given
        when(commentRepository.existsById(COMMENT_ID)).thenReturn(false);

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                commentService.deleteComment(COMMENT_ID));

        // then
        assertEquals("Comment does not exist!", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldDeleteCommentByID() {
        // given
        when(commentRepository.existsById(COMMENT_ID)).thenReturn(true);

        // when
        commentService.deleteComment(COMMENT_ID);

        // then
        verify(commentRepository).deleteById(COMMENT_ID);
    }

    @Test
    void shouldReturnAllListOfComments() {
        // given
        List<Comment> commentList = new ArrayList<>();
        commentList.add(commentId2);
        commentList.add(commentId3);

        when(commentRepository.findAll()).thenReturn(commentList);

        // when
        List<CommentDTO> results = commentService.getAllComments();
        List<CommentDTO> expected = CommentMapper.buildDTOs(commentList);

        // then
        assertEquals(expected, results);
    }

    @Test
    void shouldThrowExceptionWhenNoCommentsAreInDb() {
        //given
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                commentService.getAllComments());

        //then
        assertEquals("Comments list is empty", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCommentIdIsNull() {
        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                commentService.getCommentById(null));

        // then
        assertEquals("Comment ID is missing", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCandidateNotFoundForGetCandidateById() {
        // given
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        // when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                commentService.getCommentById(COMMENT_ID));

        // then
        assertEquals("Comment with id: " + COMMENT_ID + "not found.", resourceNotFoundException.getMessage());
    }

    @Test
    void shouldReturnCommentDTO() {
        // given
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(commentId2));

        // when
        CommentDTO result = commentService.getCommentById(COMMENT_ID);

        // then
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getCandidateId());
        assertEquals("Good job with answers", result.getComment());
    }

    @Test
    void shouldUpdateComment() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(CommentMapper.buildEntity(commentIdDTO)));
        when(commentRepository.save(any())).then(AdditionalAnswers.returnsFirstArg());

        // when
        CommentDTO result = commentService.updateComment(commentIdDTO);

        // then
        assertEquals("Good job with answers", result.getComment());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingAMissingComment() {
        //given
        CommentDTO commentDTO = CommentDTO.builder().id(99L).build();
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, () ->
                commentService.updateComment(commentDTO));

        //then
        assertEquals("Comment with id: 99 not found.", resourceNotFoundException.getMessage());
    }
}
