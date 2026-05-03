package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fitness.gymManagementSystem.config.JwtProperties;
import com.fitness.gymManagementSystem.dto.AuthResponse;
import com.fitness.gymManagementSystem.dto.LoginRequest;
import com.fitness.gymManagementSystem.dto.RegisterRequest;
import com.fitness.gymManagementSystem.dto.UserResponse;
import com.fitness.gymManagementSystem.entity.Role;
import com.fitness.gymManagementSystem.entity.User;
import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.UserRepository;
import com.fitness.gymManagementSystem.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // 1. MOCK TẤT CẢ DEPENDENCIES
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserDetailsService userDetailsService;
    @Mock private UserMapper userMapper;
    @Mock private JwtProperties jwtProperties;

    // 2. INJECT MOCKS VÀO SERVICE CẦN TEST
    @InjectMocks
    private AuthService authService;

    // 3. CHUẨN BỊ DỮ LIỆU DÙNG CHUNG (DUMMY DATA)
    private User mockUser;
    private UserResponse mockUserResponse;
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .fullName("Test User")
                .passwordHash("hashed_password")
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        mockUserResponse = new UserResponse(
                1L, "testuser", "test@gmail.com", "Test User",
                Role.USER, UserStatus.ACTIVE, null, null, 0, 0, null, null
        );

        mockUserDetails = mock(UserDetails.class);
    }

    // ==========================================
    // MODULE 1: REGISTER (ĐĂNG KÝ)
    // ==========================================

    @Test
    void register_ValidRequest_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "test@gmail.com", "Password@123", "Test User");
        
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(userMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        UserResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.username());
        
        // System Insight: Đảm bảo luồng mã hóa mật khẩu và lưu DB được gọi đúng 1 lần
        verify(passwordEncoder, times(1)).encode("Password@123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_UsernameAlreadyExists_ThrowsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest("testuser", "new@gmail.com", "Pass", "Name");
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            authService.register(request);
        });
        assertEquals("Username đã tồn tại", exception.getMessage());

        // Boundary Check: Đảm bảo DB không bao giờ thực thi lệnh lưu nếu có lỗi
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser", "test@gmail.com", "Pass", "Name");
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ==========================================
    // MODULE 2: LOGIN (ĐĂNG NHẬP)
    // ==========================================

    @Test
    void login_WithValidUsername_Success() {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "Password@123");
        
        when(userRepository.findByUsername(request.usernameOrEmail())).thenReturn(Optional.of(mockUser));
        when(userDetailsService.loadUserByUsername(mockUser.getUsername())).thenReturn(mockUserDetails);
        when(jwtService.generateAccessToken(mockUserDetails)).thenReturn("access_token_abc");
        when(jwtService.generateRefreshToken(mockUserDetails)).thenReturn("refresh_token_xyz");
        when(jwtProperties.getAccessTokenExpirationMs()).thenReturn(3600000L);
        when(userMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access_token_abc", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        
        // Verify việc xác thực bằng Spring Security AuthenticationManager đã được trigger
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_WithValidEmail_FallbackSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest("test@gmail.com", "Password@123");
        
        // Cố tình cho findByUsername trả về rỗng, để test toán tử .or()
        when(userRepository.findByUsername(request.usernameOrEmail())).thenReturn(Optional.empty());
        // Sau đó findByEmail phải tìm ra được user
        when(userRepository.findByEmail(request.usernameOrEmail())).thenReturn(Optional.of(mockUser));
        
        when(userDetailsService.loadUserByUsername(mockUser.getUsername())).thenReturn(mockUserDetails);
        when(jwtService.generateAccessToken(any())).thenReturn("token");
        when(jwtProperties.getAccessTokenExpirationMs()).thenReturn(1000L);

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        verify(userRepository).findByEmail("test@gmail.com");
    }

    @Test
    void login_UserNotFound_ThrowsBadCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("wrong_user", "password");
        
        when(userRepository.findByUsername("wrong_user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("wrong_user")).thenReturn(Optional.empty());

        // Act & Assert
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.login(request));
        assertEquals("User/Email hoặc mật khẩu không đúng", exception.getMessage());
        
        // Verify AuthenticationManager KHÔNG được gọi để tránh tốn tài nguyên mã hóa bcrypt vô ích
        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    void login_WrongPassword_ThrowsAuthenticationException() {
        // Arrange
        LoginRequest request = new LoginRequest("testuser", "wrong_pass");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        
        // Giả lập AuthenticationManager ném lỗi khi pass sai (chuẩn cơ chế Spring Security)
        doThrow(new BadCredentialsException("Bad credentials"))
            .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
        
        // Verify token KHÔNG bao giờ được sinh ra
        verify(jwtService, never()).generateAccessToken(any());
    }

    // ==========================================
    // MODULE 3: GET CURRENT USER
    // ==========================================

    @Test
    void getCurrentUser_UserExists_ReturnsUserResponse() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(userMapper.toResponse(mockUser)).thenReturn(mockUserResponse);

        // Act
        UserResponse response = authService.getCurrentUser("testuser");

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.username());
    }

    @Test
    void getCurrentUser_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.getCurrentUser("ghost");
        });
        
        // System Review: Cấu trúc message ResourceNotFoundException thường là "Entity not found with ID/Name: {name}"
        // Tùy vào cách bạn viết Exception, thông báo có thể khác nhau.
        assertTrue(exception.getMessage().contains("User"));
    }
}