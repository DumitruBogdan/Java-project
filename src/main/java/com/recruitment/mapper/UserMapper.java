package com.recruitment.mapper;

import com.recruitment.domain.User;
import com.recruitment.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User buildEntity(UserDTO userDTO) {
        return User.builder()
                .roleName(userDTO.getRoleName())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .departmentId(userDTO.getDepartmentId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

    public static UserDTO buildDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .roleName(user.getRoleName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .departmentId(user.getDepartmentId())
                .password(user.getPassword())
                .build();
    }

    public static List<UserDTO> buildDTOs(List<User> users) {
        return users.stream().map(UserMapper::buildDTO).collect(Collectors.toList());
    }
}

