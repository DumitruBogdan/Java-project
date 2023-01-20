package com.recruitment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experience")
public class Experience {
    @Id
    @Column(name = "candidate_id")
    private Long candidateId;

    private String cvName;

    @Lob
    private byte[] cv;

    @Column(name = "gdpr_name")
    private String gdprName;

    @Lob
    private byte[] gdpr;

    private String comments;
}
