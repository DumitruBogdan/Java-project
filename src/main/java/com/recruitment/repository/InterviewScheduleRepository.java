package com.recruitment.repository;

import com.recruitment.domain.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

    List<InterviewSchedule> findByUsersIdAndEndDateAfterAndStartDateBefore(Long userId,
                                                                           LocalDateTime startDate,
                                                                           LocalDateTime endDate);

    List<InterviewSchedule> findByUsersId(Long userId);
}