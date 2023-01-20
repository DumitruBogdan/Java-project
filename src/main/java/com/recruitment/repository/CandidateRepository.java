package com.recruitment.repository;

import com.recruitment.domain.Candidate;
import com.recruitment.domain.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, CustomCandidateRepository {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);
    List<Candidate> findAllByAccountStatusAndLastLoginDateBefore(AccountStatus status, LocalDateTime lastLoginDate);

    List<Candidate> findAllByAccountStatus(AccountStatus accountStatus);

    List<Candidate> findByAccountStatusIn(List<AccountStatus> accountStatuses);

    List<Candidate> findAllByLastLoginDateLessThanEqualAndLastLoginDateGreaterThanEqual(LocalDateTime endDate,
                                                                                        LocalDateTime startDate);
}
