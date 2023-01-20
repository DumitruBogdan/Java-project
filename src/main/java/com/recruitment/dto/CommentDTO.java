package com.recruitment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDTO {
    private Long id;
    @NotNull
    private Long candidateId;
    @NotNull
    private Long userId;
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String comment;
    private LocalDateTime date;
}
