package com.study.my_spring_study_diary.global.security.jwt;


import com.study.my_spring_study_diary.auth.exception.ExpiredTokenException;
import com.study.my_spring_study_diary.auth.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity
    ) {
        // 설정 secret 문자열로부터 HMAC-SHA 서명용 SecretKey 생성
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        // 초 -> 밀리초 변환
        this.accessTokenValidityInMilliseconds = accessTokenValidity * 1_000;
        // 초 -> 밀리초 변환
        this.refreshTokenValidityInMilliseconds = refreshTokenValidity * 1_000;
    }

    /**
     * Authentication 객체를 기반으로 Access Token 생성
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Objects.requireNonNull(userDetails);
        return generateAccessToken(userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","))
        );
    }

    /**
     * username과 roles를 기반으로 Access Token 생성
     */
    public String generateAccessToken(String username, String roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * username을 기반으로 Refresh Token 생성
     * Refresh Token에는 roles 정보를 포함하지 않는다.
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰에서 username 추출
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract username from token", e);
            throw new InvalidTokenException("Invalid token");
        }
    }

    /**
     * 토큰에서 권한(roles) 정보 추출
     */
    public String getRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseEncryptedClaims(token)
                    .getPayload();

            return claims.get("roles", String.class);
        } catch (Exception e) {
            log.error("Failed to extract roles from token", e);
            return "";
        }
    }

    /**
     * 토큰 유효성 검증
     * 서명이 올바른지, 만료되지 않았는지 등을 확인한다.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token has expired", e);
            throw new ExpiredTokenException("Token has expired");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
            throw new InvalidTokenException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format", e);
            throw new InvalidTokenException("Invalid JWT token format");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature", e);
            throw new InvalidTokenException("Invalid JWT signature");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
            throw new InvalidTokenException("JWT claims string is empty");
        }
    }

    /**
     * Refresh Token인지 확인
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = claims.get("type", String.class);
            return "refresh".equals(type);
        } catch (Exception e) {
            log.error("Failed to check token type", e);
            return false;
        }
    }

    /**
     * 토큰의 만료 시간 추출
     */
    public Date getExpirationFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration();
        } catch (Exception e) {
            log.error("Failed to extract expiration from token", e);
            throw new InvalidTokenException("Invalid token");
        }
    }
}
