package com.recruitment.service;

import com.recruitment.domain.Comment;
import com.recruitment.dto.CommentDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.EntityNotFoundException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CommentMapper;
import com.recruitment.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public CommentDTO createComment(CommentDTO commentDTO) {
        if (commentDTO.getCandidateId() == null) {
            log.error("Fail creating comment: {} for the candidate with null id", commentDTO.getComment());
            throw new EntityNotFoundException("Content of comment does not exists!");
        }
        Comment createdComment = commentRepository.save(CommentMapper.buildEntity(commentDTO));
        log.info("Comment was created successfully");
        return CommentMapper.buildDTO(createdComment);
    }

    public CommentDTO getCommentById(Long id) {
        if (id == null) {
            log.error("Get method failed for id being null");
            throw new ResourceNotFoundException("Comment ID is missing");
        }
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            log.error("Could not find comment with id: {}", id);
            throw new ResourceNotFoundException("Comment with id: " + id + "not found.");
        }
        return CommentMapper.buildDTO(comment.get());
    }

    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        log.debug("Current number of comments in database: {}", comments.size());
        if (comments.isEmpty()) {
            log.warn("No comments exist in database.");
            throw new ResourceNotFoundException("Comments list is empty");
        }
        return CommentMapper.buildDTOs(comments);
    }

    public CommentDTO updateComment(CommentDTO commentDTO) {
        Optional<Comment> dbComment = commentRepository.findById(commentDTO.getId());
        if (dbComment.isEmpty()) {
            log.error("Comment with id: {} not found. Can't fulfill update operation,", commentDTO.getId());
            throw new ResourceNotFoundException("Comment with id: " + commentDTO.getId() + " not found.");
        }

        Comment updatedComment = commentRepository.save(CommentMapper.buildEntity(commentDTO));
        log.info("Comment with id: {} was updated with success.", updatedComment.getId());
        return CommentMapper.buildDTO(updatedComment);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            log.warn("No comment with id: {} exist in database", id);
            throw new ResourceNotFoundException("Comment does not exist!");
        }
        commentRepository.deleteById(id);
        log.info("Comment with id: {} was deleted successfully.", id);
    }


}
