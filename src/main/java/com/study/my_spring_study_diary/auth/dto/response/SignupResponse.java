package com.study.my_spring_study_diary.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponse {

    private Long userId;
    private String username;
    private String email;
    private String message;

    /**
     * 회원가입 응답 생성 및 성공 메시지 포함
     */
    public static SignupResponse of(
            Long userId,
            String username,
            String email
    ) {
        return SignupResponse.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .message("회원가입이 성공적으로 되었습니다.")
                .build();
    }
}
