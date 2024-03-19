package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Training;
import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTitleAndUser(String title, User user);
    @Query("SELECT t FROM Training t WHERE LOWER(t.title) LIKE %:keyword%")
    List<Training> findByTitleContainingKeyword(@Param("keyword") String keyword);

}
