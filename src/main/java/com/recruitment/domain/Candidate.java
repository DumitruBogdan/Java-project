package com.recruitment.domain;

import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.domain.enums.HiredStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "candidate")
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String email;

    private String phoneNumber;

    private String country;

    private String address;

    private String username;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private LocalDateTime lastLoginDate;

    private String password;

    @Enumerated(EnumType.STRING)
    private HiredStatus hiredStatus;

    @OneToOne(mappedBy = "candidate", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Experience experience;


    @PrePersist
    public void prePersist() {
        accountStatus = AccountStatus.ACTIVE;
        lastLoginDate = LocalDateTime.now();
        hiredStatus = HiredStatus.NO_GO;
    }

    @PreUpdate
    public void preUpdate() {
        lastLoginDate = LocalDateTime.now();
    }
}
