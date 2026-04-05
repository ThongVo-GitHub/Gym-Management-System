package com.fitness.gymManagementSystem.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.fitness.gymManagementSystem.entity.UserStatus;
import com.fitness.gymManagementSystem.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.fitness.gymManagementSystem.entity.User user = userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.findByEmail(username).orElse(null));

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UsernameNotFoundException("User is not active");
        }

        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities
        );
    }
}