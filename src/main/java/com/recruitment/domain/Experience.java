package com.recruitment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experience")
public class Experience {
    @Id
    private Long id;

    private String cvName;

    @Lob
    private byte[] cv;

    @Column(name = "gdpr_name")
    private String gdprName;

    @Lob
    private byte[] gdpr;

    private String comments;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Candidate candidate;
}
