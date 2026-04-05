package com.fitness.gymManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.gymManagementSystem.dto.RegisterRequest; 
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.UserRepository;



@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(String search, Role role, Pageable pageable) {
        Page<User> users = userRepository.findAllBySearchAndRole(search, role, pageable);
        return users.map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }

        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password())) 
                .role(Role.USER) 
                .status(UserStatus.ACTIVE) 
                .build();

        User savedUser = userRepository.save(newUser);

        return userMapper.toResponse(savedUser);
}
    
    // update
    @Transactional
    public UserResponse update(Long id, User updatedUser, String currentUsername) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() != Role.ADMIN &&
            !existingUser.getUsername().equals(currentUsername)) {
            throw new RuntimeException("Bạn không có quyền sửa user này!");
        }

        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
        }

    return userMapper.toResponse(userRepository.save(existingUser));
}
    
    // delete
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponse(user);
    }
}