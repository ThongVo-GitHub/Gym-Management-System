package com.fitness.gymManagementSystem.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fitness.gymManagementSystem.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(
            userDetails.getUsername(),
            jwtProperties.getAccessTokenExpirationMs(),
            "access" // Đánh dấu là Access Token
        );
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(
            userDetails.getUsername(),
            jwtProperties.getRefreshTokenExpirationMs(),
            "refresh" // Đánh dấu là Refresh Token
        );
    }

    private String buildToken(String subject, long expirationMs, String tokenType) {
        return Jwts.builder()
            .claims(Map.of("type", tokenType)) // Thêm custom claim để nhận diện
            .subject(subject)
            .issuer("gym-api")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(getSigningKey())
            .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get("type", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResoler) {
        Claims claims = extractAllClaims(token);
        return claimsResoler.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        String type = extractTokenType(token);
        
        return username != null 
            && username.equals(userDetails.getUsername()) 
            && "access".equals(type) // Bắt buộc phải là access token
            && !isTokenExpired(token);
    }
    
    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        String type = extractTokenType(token);
        
        return username != null 
            && username.equals(userDetails.getUsername()) 
            && "refresh".equals(type) 
            && !isTokenExpired(token);
    }
}