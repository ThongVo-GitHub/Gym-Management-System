package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    // ==========================================
    // TEST QUẢN LÝ (FIND ALL, FIND BY ID, DELETE)
    // ==========================================
    @Test
    void findAll_ReturnsPage() {
        Page<User> page = new PageImpl<>(List.of(mockUser));
        when(userRepository.findAllBySearchAndRole(any(), any(), any())).thenReturn(page);
        
        UserResponse mockResponse = new UserResponse(1L, "testuser", "test@gmail.com", "Test", Role.USER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now());
        when(userMapper.toResponse(any(User.class))).thenReturn(mockResponse);

        Page<UserResponse> res = userService.findAll("test", Role.USER, PageRequest.of(0, 10));
        assertFalse(res.isEmpty());
    }

    @Test
    void findById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        UserResponse mockResponse = new UserResponse(1L, "testuser", "test@gmail.com", "Test", Role.USER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now());
        when(userMapper.toResponse(mockUser)).thenReturn(mockResponse);
        assertNotNull(userService.findById(1L));
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void delete_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        userService.delete(1L);
        verify(userRepository).delete(mockUser);
    }

    @Test
    void getByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        UserResponse mockResponse = new UserResponse(1L, "testuser", "test@gmail.com", "Test", Role.USER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now());
        when(userMapper.toResponse(mockUser)).thenReturn(mockResponse);
        assertNotNull(userService.getByUsername("testuser"));
    }

    @Test
    void getByUsername_NotFound_ThrowsException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> userService.getByUsername("ghost"));
    }

    // ==========================================
    // TEST REGISTER (Đăng ký)
    // ==========================================
    @Test
    void register_ValidRequest_ReturnsUserResponse() {
        RegisterRequest req = new RegisterRequest("newuser", "new@gmail.com", "pass123", "New User");
        
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        UserResponse mockResponse = new UserResponse(
            1L, "newuser", "new@gmail.com", "New User", 
            Role.USER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now()
        );
        when(userMapper.toResponse(any(User.class))).thenReturn(mockResponse);

        UserResponse response = userService.register(req);

        assertNotNull(response);
        assertEquals("newuser", response.username());
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        RegisterRequest req = new RegisterRequest("testuser", "new@gmail.com", "pass", "Name");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> userService.register(req));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest req = new RegisterRequest("newuser", "test@gmail.com", "pass", "Name");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> userService.register(req));
    }
    
    // ==========================================
    // TEST UPDATE (Cập nhật)
    // ==========================================
    @Test
    void update_SelfUpdate_Success() {
        UpdateUserRequest req = new UpdateUserRequest("newemail@gmail.com", "New Name", null, "newpass");        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        
        when(userRepository.existsByEmail("newemail@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass")).thenReturn("newHashed");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        UserResponse mockResponse = new UserResponse(
            1L, "testuser", "newemail@gmail.com", "New Name", 
            Role.USER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now()
        );
        when(userMapper.toResponse(any())).thenReturn(mockResponse);

        UserResponse response = userService.update(1L, req, "testuser");
        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_UnauthorizedUser_ThrowsForbidden() {
        UpdateUserRequest req = new UpdateUserRequest(null, null, null, null);
        User hacker = User.builder().id(2L).username("hacker").role(Role.USER).build();
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("hacker")).thenReturn(Optional.of(hacker));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.update(1L, req, "hacker"));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void update_AsAdmin_Success_WithRoleChange() {
        User adminUser = User.builder().id(3L).username("admin").role(Role.ADMIN).build();
        UpdateUserRequest req = new UpdateUserRequest("newemail@gmail.com", "New Name", Role.TRAINER, null); // Admin đổi quyền thành TRAINER
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        when(userRepository.existsByEmail("newemail@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        UserResponse mockResponse = new UserResponse(
            1L, "testuser", "newemail@gmail.com", "New Name", 
            Role.TRAINER, UserStatus.ACTIVE, null, null, 0, 0, Instant.now(), Instant.now()
        );
        when(userMapper.toResponse(any())).thenReturn(mockResponse);

        userService.update(1L, req, "admin");
        
        verify(userRepository).save(mockUser);
        assertEquals(Role.TRAINER, mockUser.getRole()); // Đảm bảo quyền đã thực sự bị đổi
    }

    @Test
    void update_DuplicateEmail_ThrowsException() {
        UpdateUserRequest req = new UpdateUserRequest("exist@gmail.com", null, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByEmail("exist@gmail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> userService.update(1L, req, "testuser"));
    }
}