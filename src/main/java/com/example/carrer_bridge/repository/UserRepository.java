package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) FROM User u WHERE u.firstName = ?1")
    boolean existsByFirstName(String firstName);
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastName = ?1")
    boolean existsByLastName(String lastName);
    @Query("SELECT COUNT(u) FROM User u WHERE u.email = ?1")
    boolean existsByEmail(String email);

}
