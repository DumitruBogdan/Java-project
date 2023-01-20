package com.recruitment.service;

import com.recruitment.base.UserBaseTest;
import com.recruitment.domain.InterviewSchedule;
import com.recruitment.domain.User;
import com.recruitment.dto.UserDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.EntityNotFoundException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.UserMapper;
import com.recruitment.repository.InterviewScheduleRepository;
import com.recruitment.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest extends UserBaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterviewScheduleRepository interviewScheduleRepository;

    @InjectMocks
    private UserService service;

    @BeforeEach
    void setUp() {
        init();
    }


    @Test
    void shouldCreateNewUser() {
        // given
        User savedUser = UserMapper.buildEntity(dto);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        service.create(dto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        // then
        assertThat(capturedUser).isEqualTo(savedUser);
    }

    @Test
    void shouldCreateUserWithPassword() {
        // given
        User savedUser = UserMapper.buildEntity(dto);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // when
        UserDTO saved = service.create(dto);

        // then
        assertThat(saved.getPassword()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenCreatingUserWithDuplicateEmail() {
        // given
        given(userRepository.existsByEmail(email)).willReturn(true);

        // then
        assertThatThrownBy(() -> service.create(UserMapper.buildDTO(user)))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    void shouldThrowExceptionWhenCannotFindUserToUpdate() {
        // given
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(UserMapper.buildDTO(user)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id: " + id + " not found");
    }

    @Test
    void shouldUpdateUser() {
        // given
        anotherDTO.setId(anotherId);
        given(userRepository.findById(anotherId)).willReturn(Optional.of(anotherUser));
        when(userRepository.save(any(User.class))).thenReturn(anotherUser);

        // then
        service.update(anotherDTO);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        // then
        assertThat(capturedUser).isEqualTo(anotherUser);
    }

    @Test
    void shouldUpdateUserPassword() {
        // given
        dto.setId(id);
        String oldPassword = "password";
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        user.setPassword("anotherPassword");
        dto.setPassword("anotherPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        service.update(dto);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> saved = service.findByEmail(email);
        // then
        assertThat(saved.get().getPassword()).isNotEqualTo(oldPassword);
    }

    @Test
    void shouldGetAllUsers() {
        // given
        List<User> users = Arrays.asList(user, anotherUser);
        given(userRepository.findAll()).willReturn(users);

        // when
        service.getAllUsers();

        // then
        verify(userRepository).findAll();
        assertThat(UserMapper.buildDTOs(users)).isEqualTo(service.getAllUsers());
    }

    @Test
    void shouldReturnExceptionWhenNoUsersInDb() {
        // given
        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> service.getAllUsers())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("No users in database");
    }

    @Test
    void shouldGetUserById() {
        // given
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // when
        UserDTO found = service.getUserById(id);

        // then
        verify(userRepository).findById(id);
        assertThat(found).isEqualTo(UserMapper.buildDTO(user));
    }

    @Test
    void shouldThrowExceptionWhenCannotGetUserById() {
        // given
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.getUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id: " + id + " was not found.");
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        // when
        service.findByEmail(email);

        // then
        verify(userRepository).findByEmail(email);
        assertThat(service.findByEmail(email).get()).isEqualTo(user);
    }

    @Test
    void shouldDeleteUserById() {
        // given
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // when
        service.deleteUser(id);

        // then
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenCannotFindUserToDelete() {
        // given
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.deleteUser(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User with id: " + id + " was not found");
    }

    @Test
    void shouldReturnAssignedCandidatesId() {
        // given
        Long id = 1L;
        InterviewSchedule interviewSchedule = InterviewSchedule.builder().candidateId(id).build();
        given(interviewScheduleRepository.findByUsersId(id)).willReturn(Collections.singletonList(interviewSchedule));

        // when
        List<Long> resultedCandidatesId = service.getAssignedCandidates(id);

        // then
        assertThat(resultedCandidatesId.get(0)).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionWhenUserHasNoCandidates() {
        // given
        given(interviewScheduleRepository.findByUsersId(id)).willReturn(Collections.emptyList());

        // then
        assertThatThrownBy(() -> service.getAssignedCandidates(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No assigned interviews found!");
    }
}
