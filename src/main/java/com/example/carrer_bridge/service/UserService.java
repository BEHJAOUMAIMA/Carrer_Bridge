package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    User update(User userUpdated, Long id);
    void delete(Long id);

}
