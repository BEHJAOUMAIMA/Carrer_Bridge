package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.firstName = ?1")
    Boolean existsByFirstName(String firstName);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.lastName = ?1")
    Boolean existsByLastName(String lastName);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1")
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    List<User> findByRole_RoleType(RoleType roleType);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.skills WHERE u.id = :userId")
    Optional<User> findByIdWithSkills(Long userId);


}
