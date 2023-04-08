package com.recruitment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.base.UserBaseTest;
import com.recruitment.domain.User;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.UserMapper;
import com.recruitment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerFunctionalTest extends UserBaseTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        List<User> userList = Arrays.asList(user, anotherUser);
        given(userService.getAllUsers()).willReturn(UserMapper.buildDTOs(userList));

        mvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(userService).getAllUsers();
    }

    @Test
    void shouldGetUserById() throws Exception {
        given(userService.getUserById(id)).willReturn(UserMapper.buildDTO(user));

        mvc.perform(get("/api/users/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.roleName").value(dto.getRoleName().name()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()));
        verify(userService).getUserById(id);
    }

    @Test
    void shouldReturnBadRequestWhenCannotFindUserById() throws Exception {
        given(userService.getUserById(id)).willThrow(ResourceNotFoundException.class);

        mvc.perform(get("/api/users/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldCreateUser() throws Exception {

        given(userService.create(any())).willReturn(dto);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value(dto.getRoleName().name()))
                .andExpect(jsonPath("$.departmentId").value(dto.getDepartment()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()));
    }

    @Test
    void shouldReturnBadRequestWhenCannotCreateUserWhenDuplicateEmail() throws Exception {
        given(userService.create(any())).willThrow(EntityAlreadyExistsException.class);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldUpdateUser() throws Exception {

        given(userService.update(any())).willReturn(dto);

        mvc.perform(put("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value(dto.getRoleName().name()))
                .andExpect(jsonPath("$.departmentId").value(dto.getDepartment()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.email").value(dto.getEmail()));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(id)))
                .andExpect(status().isOk());
        verify(userService).deleteUser(id);
    }
}