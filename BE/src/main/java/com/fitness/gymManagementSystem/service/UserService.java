package com.fitness.gymManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fitness.gymManagementSystem.dto.RegisterRequest;
import com.fitness.gymManagementSystem.dto.UpdateUserRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.UserRepository;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                    PasswordEncoder passwordEncoder,
                    UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(String search, Role role, Pageable pageable) {
        return userRepository.findAllBySearchAndRole(search, role, pageable)
                .map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User", id)));
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.username());

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .fullName(request.fullName())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        return userMapper.toResponse(userRepository.save(newUser));
    }

    // Nhận UpdateUserRequest DTO thay vì raw User entity
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request, String currentUsername) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isSelf = existingUser.getUsername().equals(currentUsername);
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isSelf && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền sửa user này");
        }

        if (request.email() != null && !request.email().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException("Email đã tồn tại");
            }
            existingUser.setEmail(request.email());
        }

        if (request.fullName() != null) {
            existingUser.setFullName(request.fullName());
        }

        // Chỉ ADMIN mới được đổi role
        if (request.role() != null && isAdmin) {
            existingUser.setRole(request.role());
        }

        if (request.newPassword() != null && !request.newPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        }

        return userMapper.toResponse(userRepository.save(existingUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        return userMapper.toResponse(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User không tồn tại")));
    }
}