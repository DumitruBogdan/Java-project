package com.recruitment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recruitment.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {
    private Long id;

    private Role roleName;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotNull
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotNull
    private Integer departmentId;

    @NotNull
    @NotBlank
    @Size(max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}

