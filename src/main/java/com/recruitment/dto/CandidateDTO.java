package com.recruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recruitment.domain.enums.AccountStatus;
import com.recruitment.domain.enums.HiredStatus;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CandidateDTO {
    private Long id;
    @NotNull
    @NotBlank
    private String firstName;
    @NotNull
    @NotBlank
    private String lastName;
    @NotNull
    @NotBlank
    @Length(min = 4, max = 6)
    private String gender;
    @NotNull
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;
    @NotNull
    @NotBlank
    @Size(max = 12)
    private String phoneNumber;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String country;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String address;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Size(max = 100)
    private String password;
    private AccountStatus accountStatus;
    private LocalDateTime lastLoginDate;
    private HiredStatus hiredStatus;
}
