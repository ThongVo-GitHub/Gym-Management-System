package com.fitness.gymManagementSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF (vì dùng JWT)
            .csrf(csrf -> csrf.disable())

            // ❌ Không dùng session (JWT stateless)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 PHÂN QUYỀN
            .authorizeHttpRequests(auth -> auth

                // ===== PUBLIC =====
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()

                // ===== ADMIN =====
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // ===== USER + ADMIN =====
                .requestMatchers("/api/users/**")
                    .hasAnyRole("USER", "ADMIN")

                // ===== TRAINER tạo lớp =====
                .requestMatchers(HttpMethod.POST, "/api/classes")
                    .hasRole("TRAINER")

                // ===== USER book lớp =====
                .requestMatchers(HttpMethod.POST, "/api/classes/*/book")
                    .hasRole("USER")

                // ===== XEM LỚP =====
                .requestMatchers(HttpMethod.GET, "/api/classes/**")
                    .hasAnyRole("USER", "TRAINER", "ADMIN")

                // ===== CÒN LẠI =====
                .anyRequest().authenticated()
                
                .requestMatchers(HttpMethod.GET, "/api/users/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                    .hasRole("ADMIN")
            )

            // 🔥 HANDLE ERROR
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401
                .accessDeniedHandler(accessDeniedHandler)             // 403
            )

            // 🔥 JWT FILTER
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}