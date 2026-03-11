package com.study.my_spring_study_diary.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 로그인 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3자에서 50자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자에서 100자 사이여야 합니다.")
    private String password;
}
