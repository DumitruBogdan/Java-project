package com.recruitment.repository;

import com.recruitment.domain.Candidate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomCandidateRepositoryImpl implements CustomCandidateRepository {
    @Autowired
    private EntityManager em;

    public List<Candidate> findByColumnAndKeyword(String column, String keyword) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Candidate> criteriaQuery = criteriaBuilder.createQuery(Candidate.class);
        Root<Candidate> candidateRoot = criteriaQuery.from(Candidate.class);
        Predicate predicate = criteriaBuilder.like(candidateRoot.get(column).as(String.class), "%" + keyword + "%");

        criteriaQuery.where(predicate);
        TypedQuery<Candidate> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Candidate> findByKeyword(String keyword) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Candidate> criteriaQuery = criteriaBuilder.createQuery(Candidate.class);
        Root<Candidate> candidateRoot = criteriaQuery.from(Candidate.class);
        Predicate predicate = criteriaBuilder.or(
                criteriaBuilder.like(candidateRoot.get("id").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("firstName").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("lastName").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("gender").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("email").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("phoneNumber").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("country").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("address").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("userName").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("accountStatus").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.like(candidateRoot.get("lastLoginDate").as(String.class), "%" + keyword + "%"),
                criteriaBuilder.equal(candidateRoot.get("hiredStatus").as(String.class), "%" + keyword + "%"));

        criteriaQuery.where(predicate);
        TypedQuery<Candidate> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}