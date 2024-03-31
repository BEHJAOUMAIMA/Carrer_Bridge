package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.JobOpportunity;
import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {
    boolean existsByTitleAndUser_IdAndCompany_Id(String title, Long userId, Long companyId);
    List<JobOpportunity> findByUser(User currentUser);

    @Query("SELECT j FROM JobOpportunity j " +
            "JOIN j.company c " +
            "JOIN j.city city " +
            "WHERE (:title IS NULL OR j.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:companyName IS NULL OR c.name LIKE CONCAT('%', :companyName, '%')) " +
            "AND (:jobCity IS NULL OR city.name LIKE CONCAT('%', :jobCity, '%'))")
    List<JobOpportunity> filterJobOpportunities(@Param("title") String title,
                                                @Param("companyName") String companyName,
                                                @Param("jobCity") String jobCity);
}
