package com.fitness.gymManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            UserMapper userMapper,
            JwtProperties jwtProperties
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.jwtProperties = jwtProperties;
    }

    // TỐI ƯU: Bắt buộc phải có @Transactional cho thao tác INSERT
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username đã tồn tại");
        }
        if(userRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("Email đã tồn tại");
        }

        // TỐI ƯU: Đã bổ sung fullName và status từ code cũ của UserService sang đây
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .fullName(request.fullName())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);
        log.info("User registered: {}", user.getFullName());
        
        return userMapper.toResponse(user);
    }

    // (Hàm login và getCurrentUser giữ nguyên như cũ vì bạn đã làm rất tốt)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.usernameOrEmail())
                    .or(() ->  userRepository.findByEmail(request.usernameOrEmail()))
                    .orElseThrow(() -> new BadCredentialsException("User/Email hoặc mật khẩu không đúng"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.password()));

        var userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        log.info("User logged in: {}", user.getUsername());

        return AuthResponse.of(
            accessToken,
            refreshToken,
            "Bearer",
            jwtProperties.getAccessTokenExpirationMs(),
            userMapper.toResponse(user)
        );
    }

    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", username));
        return userMapper.toResponse(user);
    }
}