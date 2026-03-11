package com.study.my_spring_study_diary.global.security.jwt;

import com.study.my_spring_study_diary.auth.exception.ExpiredTokenException;
import com.study.my_spring_study_diary.auth.exception.InvalidTokenException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 인증 필터
 * 요청당 한번 만 실행되도록 OncePerRequestFilter를 확장합니다.
 * Authorization 헤더에서 JWT를 추출하고 사용자를 인증합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 1. 요청 헤더에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);

            // 2. 토큰이 존재하면 인증 수행
            if (token != null) {
                authenticateUser(token, request);
            }
        } catch (ExpiredTokenException | InvalidTokenException e) {
            // JWT 관련 예외는 request에 저장하여 EntryPoint에서 처리
            log.error("JWT authentication failed: {}", e.getMessage());
            request.setAttribute("exception", e);
        } catch (Exception e) {
            log.error("Unexpected error during JWT authentication", e);
            request.setAttribute("exception", e);
        }

        // 3. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 "Bearer" 접두사를 제거하고 토큰 값만 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * JWT 토큰을 검증하고, 유효하면 SecurityContext에 인증 정보를 설정
     */
    private void authenticateUser(String token, HttpServletRequest request) {
        // 토큰 유효성 검증
        if (jwtTokenProvider.validateToken(token)) {
            //로그인할 때 Access Token을 만들 때 넣었던 값들 중에서 username 꺼내기
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Role(권한 정보) 꺼내기
            String rolesString = jwtTokenProvider.getRolesFromToken(token);
            List<SimpleGrantedAuthority> authorities = parseAuthorities(rolesString);

            // Spring Security에서 사용하는 UserDetails 객체 생성
            UserDetails userDetails = User.builder()
                    .username(username)
                    .password("")
                    .authorities(authorities)
                    .build();

            // 인증된 사용자의 정보를 담고 있는 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 저장한다.
            // 이렇게 저장을 해주는 이유는 -> "Spring Boot 영역에서 비즈니스 로직을 실행핧 때,
            // 유저의 정보가 필요한 경우, 손쉽게 빼서 사용할 수 있도록 하기 위해서"
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authenticated user: {}", username);
        }
    }

    /**
     * 쉼표로 구분된 roles 문자열을 GrantedAuthority 리스트로 변환
     * 예: "ROLE_USER, ROLE_ADMIN" -> [SimpleGrantedAuthority("ROLE_USER"), SimpleGrantedAuthority("ROLE_ADMIN")]
     */
    private List<SimpleGrantedAuthority> parseAuthorities(String rolesString) {
        if (rolesString == null || rolesString.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(rolesString.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}