package com.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recruitment.domain.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "interviewSchedules")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role roleName;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name="department_id", nullable = false)
    private Department department;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(mappedBy = "users")
    List<InterviewSchedule> interviewSchedules;
}
