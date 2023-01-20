package com.recruitment.service;

import com.recruitment.domain.InterviewSchedule;
import com.recruitment.domain.User;
import com.recruitment.dto.UserDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.EntityNotFoundException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.UserMapper;
import com.recruitment.repository.InterviewScheduleRepository;
import com.recruitment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InterviewScheduleRepository interviewScheduleRepository;

    public UserDTO create(UserDTO createUser) {
        if (userRepository.existsByEmail(createUser.getEmail())) {
            log.error("Create user failed for email {} being duplicate", createUser.getEmail());
            throw new EntityAlreadyExistsException("User already exists!");
        }
        User createdUser = userRepository.save(UserMapper.buildEntity(createUser));
        log.info("Created user with id: " + createdUser.getId());
        return UserMapper.buildDTO(createdUser);
    }

    public UserDTO update(UserDTO userDTO) {
        Optional<User> dbUser = userRepository.findById(userDTO.getId());
        if (dbUser.isEmpty()) {
            log.error("User with id: {} not found. Can't fulfill update operation.", userDTO.getId());
            throw new ResourceNotFoundException("User with id: " + userDTO.getId() + " not found.");
        }
        Optional<User> userWithSameEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userWithSameEmail.isPresent()) {
            if (userWithSameEmail.get().getId() != userDTO.getId()) {
                log.error("User with email: {} exists. Can't fulfill update operation.", userDTO.getEmail());
                throw new EntityAlreadyExistsException("Email already in database");
            }
        }
        User intermediaryUser = UserMapper.buildEntity(userDTO);
        intermediaryUser.setId(userDTO.getId());
        intermediaryUser.setPassword(dbUser.get().getPassword());
        User updatedUser = userRepository.save(intermediaryUser);
        log.info("User with id: {} was updated with success.", updatedUser.getId());
        return UserMapper.buildDTO(updatedUser);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Current number of users in database: {}", users.size());
        if (users.isEmpty()) {
            log.warn("No users exist in database.");
            throw new EntityNotFoundException("No users in database");
        }
        return UserMapper.buildDTOs(users);
    }

    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.error("Could not find candidate with id: {}", id);
            throw new ResourceNotFoundException("User with id: " + id + " was not found.");
        }
        return UserMapper.buildDTO(user.get());
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            log.error("User with id: {} not found. Can't fulfill delete operation.", id);
            throw new ResourceNotFoundException("User with id: " + id + " was not found");
        }
    }

    public List<Long> getAssignedCandidates(Long id) {
        List<Long> assignedInterviewCandidateDTOS = new ArrayList<>();
        List<InterviewSchedule> assignedInterviews = interviewScheduleRepository.findByUsersId(id);

        if (assignedInterviews.isEmpty()) {
            log.error("No assigned interviews.");
            throw new ResourceNotFoundException("No assigned interviews found!");
        }

        for (InterviewSchedule assignedInterview : assignedInterviews) {
            assignedInterviewCandidateDTOS.add(assignedInterview.getCandidateId());
        }

        return assignedInterviewCandidateDTOS;
    }
}
