package com.recruitment.repository;

import com.recruitment.domain.Candidate;

import java.util.List;

public interface CustomCandidateRepository {
    List<Candidate> findByColumnAndKeyword(String column, String keyword);
    List<Candidate> findByKeyword(String keyword);
}
