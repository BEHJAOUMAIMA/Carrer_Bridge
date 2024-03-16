package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.mappers.RoleMapper;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.RoleService;
import com.example.carrer_bridge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final RoleService roleService;
    @Override
    public User save(User user) {
        try {
            if (userRepository.existsByFirstName(user.getFirstName())) {
                throw new OperationException("Firstname already exists!");
            }
            if (userRepository.existsByLastName(user.getLastName())) {
                throw new OperationException("Lastname already exists!");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OperationException("Email Address already exists!");
            }

            Role role = roleMapper.toEntity(user.getRole());
            user.setRole(role);

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new OperationException("Unique constraint violation" + e);
        }

    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new OperationException("No Users found");
        }

        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id <= 0) {
            throw new OperationException("ID must be greater than 0");
        }
        return userRepository.findById(id);
    }

    @Override
    public User update(User userUpdated, Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userUpdated.getFirstName());
                    user.setLastName(userUpdated.getLastName());
                    user.setEmail(userUpdated.getEmail());
                    user.setPassword(userUpdated.getPassword());
                    user.setRole(user.getRole());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new OperationException("User not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        if (id <= 0) {
            throw new OperationException("ID must be greater than 0");
        }

        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new OperationException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }
}
