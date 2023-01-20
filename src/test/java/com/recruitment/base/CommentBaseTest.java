package com.recruitment.base;

import com.recruitment.domain.Comment;
import com.recruitment.dto.CommentDTO;

import java.time.LocalDateTime;

public class CommentBaseTest {

    protected final Long COMMENT_ID = 2L;
    protected Comment commentId2;
    protected Comment commentId3;
    protected CommentDTO commentDTO;
    protected CommentDTO commentResponseDTO;
    protected CommentDTO commentIdDTO;

    public void init() {
        commentDTO = CommentDTO.builder()
                .id(1L)
                .candidateId(1L)
                .userId(1L)
                .comment("Good job with answers")
                .date(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        commentResponseDTO = CommentDTO.builder()
                .id(1L)
                .candidateId(1L)
                .userId(1L)
                .comment("Good job with answers")
                .date(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        commentId2 = Comment.builder()
                .id(2L)
                .candidateId(1L)
                .userId(1L)
                .comment("Good job with answers")
                .date(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();

        commentId3 = Comment.builder()
                .id(3L)
                .candidateId(1L)
                .userId(1L)
                .comment("Good job with answers")
                .date(LocalDateTime.of(2017, 2, 13, 15, 56))
                .build();

        commentIdDTO = CommentDTO.builder()
                .id(1L)
                .candidateId(4L)
                .userId(5L)
                .comment("Good job with answers")
                .date(LocalDateTime.parse("2020-03-15T14:15:10"))
                .build();
    }
}
