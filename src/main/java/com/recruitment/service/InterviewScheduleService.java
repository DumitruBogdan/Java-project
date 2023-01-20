package com.recruitment.service;

import com.recruitment.domain.Candidate;
import com.recruitment.domain.InterviewFeedback;
import com.recruitment.domain.InterviewSchedule;
import com.recruitment.domain.User;
import com.recruitment.domain.enums.InterviewType;
import com.recruitment.domain.enums.Role;
import com.recruitment.dto.InterviewFeedbackDTO;
import com.recruitment.dto.InterviewScheduleDTO;
import com.recruitment.exception.EntityAlreadyExistsException;
import com.recruitment.exception.RequestValidationException;
import com.recruitment.exception.ResourceNotFoundException;
import com.recruitment.mapper.CandidateMapper;
import com.recruitment.mapper.InterviewFeedbackMapper;
import com.recruitment.mapper.InterviewScheduleMapper;
import com.recruitment.repository.InterviewFeedbackRepository;
import com.recruitment.repository.InterviewScheduleRepository;
import com.recruitment.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterviewScheduleService {

    private static final int MINIMUM_HR_RECRUITERS_GUESTS = 2;
    private static final int MINIMUM_PTE_GUESTS = 1;
    private static final int MINIMUM_TECHNICAL_INTERVIEWERS_GUESTS = 2;
    private static final int DEFAULT_VALUE_IF_USER_TYPE_NOT_FOUND = 0;


    @Autowired
    InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    InterviewFeedbackRepository interviewFeedbackRepository;

    @Autowired
    CandidateService candidateService;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public InterviewScheduleDTO createInterview(InterviewScheduleDTO interviewScheduleDTO) {

        validateCandidate(interviewScheduleDTO.getCandidateId());

        List<Long> userIds = interviewScheduleDTO.getUserIds();
        List<User> usersDB = userRepository.findByIdIn(interviewScheduleDTO.getUserIds());

        validateUserIdsList(usersDB, userIds);
        validateInterviewUserRequirements(usersDB, interviewScheduleDTO.getInterviewType());
        validateUserInterviewSchedules(interviewScheduleDTO, userIds);

        InterviewSchedule interviewDB = InterviewScheduleMapper.buildEntity(interviewScheduleDTO);
        interviewDB.setUsers(usersDB);
        InterviewSchedule savedInterview = interviewScheduleRepository.save(interviewDB);

        return InterviewScheduleMapper.buildReadDTO(savedInterview, getUserNames(savedInterview));
    }

    public InterviewScheduleDTO getInterviewById(Long id) {
        InterviewSchedule interviewSchedule = interviewScheduleRepository.getReferenceById(id);
        return InterviewScheduleMapper.buildReadDTO(interviewSchedule, getUserNames(interviewSchedule));
    }

    public void deleteInterviewScheduleById(Long interviewId) {

        Optional<InterviewSchedule> interview = interviewScheduleRepository.findById(interviewId);
        if (interview.isPresent()) {
            interviewScheduleRepository.deleteById(interviewId);
        } else {
            log.error("Interview with id: {} not found. Can't fulfill delete operation.", interviewId);
            throw new ResourceNotFoundException("The interview desired to be deleted do not exist");
        }
    }

    @Transactional
    public InterviewScheduleDTO updateInterviewSchedule(InterviewScheduleDTO interviewScheduleDTO) {

        Optional<InterviewSchedule> dbInterview = interviewScheduleRepository.findById(interviewScheduleDTO.getId());
        if (dbInterview.isEmpty()) {
            log.error("Interview with interviewId: {} not found. Can't fulfill update operation.", interviewScheduleDTO.getId());
            throw new ResourceNotFoundException("Interview with interviewId: " + interviewScheduleDTO.getId() + "not found.");
        }
        InterviewSchedule updateInterviewSchedule = dbInterview.get();
        List<User> originalUsers = updateInterviewSchedule.getUsers();
        Candidate candidate = null;
        if (interviewScheduleDTO.getStartDate() != null) {
            updateInterviewSchedule.setStartDate(interviewScheduleDTO.getStartDate());
        }
        if (interviewScheduleDTO.getEndDate() != null) {
            updateInterviewSchedule.setEndDate(interviewScheduleDTO.getEndDate());
        }
        if (interviewScheduleDTO.getCandidateId() != null) {
            candidate = validateCandidate(interviewScheduleDTO.getCandidateId());
            updateInterviewSchedule.setCandidateId(interviewScheduleDTO.getCandidateId());
        }
        if (interviewScheduleDTO.getAppliedDepartmentId() < 5) {
            updateInterviewSchedule.setAppliedDepartmentId(interviewScheduleDTO.getAppliedDepartmentId());
        }
        if (interviewScheduleDTO.getInterviewType() != null) {
            updateInterviewSchedule.setInterviewType(interviewScheduleDTO.getInterviewType());
        }
        if (interviewScheduleDTO.getUserIds() != null) {
            List<Long> userIds = interviewScheduleDTO.getUserIds();
            List<User> usersDB = userRepository.findByIdIn(interviewScheduleDTO.getUserIds());
            validateUserIdsList(usersDB, userIds);
            validateInterviewUserRequirements(usersDB, updateInterviewSchedule.getInterviewType());
            validateUserInterviewSchedules(interviewScheduleDTO, userIds);
            updateInterviewSchedule.setUsers(usersDB);
        }
        InterviewSchedule updatedInterview = interviewScheduleRepository.save(updateInterviewSchedule);

        if (candidate == null) {
            candidate = validateCandidate(updatedInterview.getCandidateId());
        }

        log.info("Interview with interviewId: {} was updated with success.", updatedInterview.getId());
        return InterviewScheduleMapper.buildReadDTO(updatedInterview, getUserNames(updatedInterview));
    }

    public Candidate validateCandidate(Long id) {
        return CandidateMapper.buildEntity(candidateService.getCandidateById(id));
    }

    public void validateUserIdsList(List<User> users, List<Long> userIds) {
        if (userIds.isEmpty()) {
            throw new ResourceNotFoundException("User id list is empty");
        } else {
            if (users.size() != userIds.size()) {
                List<Long> databaseIds = users.stream().map(User::getId).collect(Collectors.toList());
                List<Long> invalidUserIds = userIds.stream()
                        .filter(userId -> !databaseIds.contains(userId))
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(invalidUserIds)) {
                    throw new ResourceNotFoundException("Users with ids: " + invalidUserIds + " do not exist");
                }
            }
        }
    }

    private void validateInterviewUserRequirements(List<User> usersDB, InterviewType interviewType) {
        Map<Role, Integer> userTypes = countUserTypes(usersDB);

        if (interviewType.equals(InterviewType.HR) || interviewType.equals(InterviewType.HR_AND_TECHNICAL)) {
            validateHRInterview(userTypes);
        }
        if (interviewType.equals(InterviewType.TECHNICAL) || interviewType.equals(InterviewType.HR_AND_TECHNICAL)) {
            validateTechnicalInterview(userTypes);
        }
    }

    private Map<Role, Integer> countUserTypes(List<User> usersDB) {
        Map<Role, Integer> userTypes = new LinkedHashMap<>();
        for (User user : usersDB) {
            if (userTypes.containsKey(user.getRoleName())) {
                userTypes.put(user.getRoleName(), userTypes.get(user.getRoleName()) + 1);
            } else {
                userTypes.put(user.getRoleName(), 1);
            }
        }

        return userTypes;
    }

    private void validateHRInterview(Map<Role, Integer> userTypes) {
        int numberOfHRRecruiters = userTypes.getOrDefault(Role.HR_REPRESENTATIVE, DEFAULT_VALUE_IF_USER_TYPE_NOT_FOUND);
        int numberOfPTE = userTypes.getOrDefault(Role.PTE, DEFAULT_VALUE_IF_USER_TYPE_NOT_FOUND);

        if (numberOfHRRecruiters < MINIMUM_HR_RECRUITERS_GUESTS) {
            log.error("Interview validation error, not enough HR recruiters guests");
            throw new RequestValidationException("The minimum number of HR recruiters is: "
                    + MINIMUM_HR_RECRUITERS_GUESTS + ", current number is: " + numberOfHRRecruiters);
        }
        if (numberOfPTE < MINIMUM_PTE_GUESTS) {
            log.error("Interview validation error, not enough PTE guests");
            throw new RequestValidationException("The minimum number of PTE guests is: " +
                    MINIMUM_PTE_GUESTS + ", current number is: " + numberOfPTE);
        }
    }

    private void validateTechnicalInterview(Map<Role, Integer> userTypes) {
        int numberOfHRTechnicalInterviewers = userTypes.getOrDefault(Role.TECHNICAL_INTERVIEWER, DEFAULT_VALUE_IF_USER_TYPE_NOT_FOUND);

        if (numberOfHRTechnicalInterviewers < MINIMUM_TECHNICAL_INTERVIEWERS_GUESTS) {
            log.error("Interview validation error, not enough technical interviewers guests");
            throw new RequestValidationException("The minimum number of technical interviewers is: " +
                    MINIMUM_TECHNICAL_INTERVIEWERS_GUESTS + ", current number is: " + numberOfHRTechnicalInterviewers);
        }
    }

    public void validateUserInterviewSchedules(InterviewScheduleDTO interviewScheduleDTO, List<Long> userIds) {
        if (!interviewScheduleDTO.getStartDate().isBefore(interviewScheduleDTO.getEndDate())) {
            log.error("Start date must be before end date.");
            throw new RequestValidationException("Start date must be before end date.");
        }
        for (Long userId : userIds) {
            List<InterviewSchedule> interviews = interviewScheduleRepository.findByUsersIdAndEndDateAfterAndStartDateBefore(
                    userId, interviewScheduleDTO.getStartDate(), interviewScheduleDTO.getEndDate());

            if (!interviews.isEmpty()) {
                for (InterviewSchedule interview : interviews) {
                    if (!interview.getId().equals(interviewScheduleDTO.getId())) {
                        log.error("User with id: {} has an interview in the same date.", userId);
                        throw new RequestValidationException("User with id " + userId + " has an interview in the same date.");
                    }
                }
            }
        }
    }

    @Transactional
    public InterviewFeedbackDTO addTechInterviewFeedback(InterviewFeedbackDTO interviewFeedbackDTO) {
        Long userId = interviewFeedbackDTO.getUserId();
        Long interviewId = interviewFeedbackDTO.getInterviewId();

        validateTechUser(userId);
        validateInterview(interviewId, interviewFeedbackDTO.getFeedbackDate());
        validateDuplicateFeedback(interviewId, userId);

        InterviewFeedback interviewFeedbackDB = InterviewFeedbackMapper.buildEntity(interviewFeedbackDTO);
        InterviewFeedback savedInterview = interviewFeedbackRepository.save(interviewFeedbackDB);

        return InterviewFeedbackMapper.buildDTO(savedInterview);
    }

    private void validateTechUser(Long userId) {
        Optional<User> techUser = userRepository.findById(userId);

        if (techUser.isPresent() && !techUser.get().getRoleName().equals(Role.TECHNICAL_INTERVIEWER)) {
            log.error("The user's role should be technical interviewer!");
            throw new RequestValidationException("The user is not a technical interviewer!");
        }
        if (techUser.isEmpty()) {
            log.error("The user was not found in the database!");
            throw new ResourceNotFoundException("User not found!");
        }
    }

    private void validateInterview(Long interviewId, LocalDateTime feedbackDate) {
        Optional<InterviewSchedule> interview = interviewScheduleRepository.findById(interviewId);
        if (interview.isPresent() && interview.get().getEndDate().isAfter(feedbackDate)) {
            log.error("The interview feedback should be after the interview is done!");
            throw new RequestValidationException("The interview feedback was written before the interview was done!");
        }
        if (interview.isEmpty()) {
            log.error("Interview with id: {} not found. Can't add feedback.", interviewId);
            throw new ResourceNotFoundException("The interview does not exist!");
        }
    }

    private void validateDuplicateFeedback(Long interviewId, Long userId) {
        List<InterviewFeedback> interviewFeedbacks = interviewFeedbackRepository.findByInterviewId(interviewId);

        for (InterviewFeedback interviewFeedback : interviewFeedbacks) {
            if (interviewFeedback.getUserId().equals(userId)) {
                log.error("The user with id {} already added a feedback message to this interview!", userId);
                throw new EntityAlreadyExistsException("The user can only add one feedback message to an interview!");
            }
        }
    }

    private List<String> getUserNames(InterviewSchedule interviewSchedule) {
        return interviewSchedule.getUsers().stream()
            .map(user -> user.getFirstName() + " " + user.getLastName())
            .collect(Collectors.toList());
    }
}
