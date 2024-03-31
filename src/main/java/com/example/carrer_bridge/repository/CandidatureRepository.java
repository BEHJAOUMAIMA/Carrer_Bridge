package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByUserIdAndJobOpportunityId(Long userId, Long jobOpportunityId);
    List<Candidature> findByUserId(Long userId);
    List<Candidature> findByJobOpportunityId(Long jobOpportunityId);
    List<Candidature> findByStatus(CandidatureStatus status);
    @Query("SELECT c FROM Candidature c JOIN c.jobOpportunity j WHERE j.user = :user")
    List<Candidature> findByJobOpportunityUser(@Param("user") User user);
    List<Candidature> findByUser(User user);


}
