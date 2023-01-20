package com.recruitment.mapper;

import com.recruitment.domain.Comment;
import com.recruitment.dto.CommentDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment buildEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .candidateId(commentDTO.getCandidateId())
                .userId(commentDTO.getUserId())
                .comment(commentDTO.getComment())
                .date(LocalDateTime.now())
                .build();
    }

    public static CommentDTO buildDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .candidateId(comment.getCandidateId())
                .userId(comment.getUserId())
                .comment(comment.getComment())
                .date(LocalDateTime.now())
                .build();
    }

    public static List<CommentDTO> buildDTOs(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::buildDTO)
                .collect(Collectors.toList());
    }
}