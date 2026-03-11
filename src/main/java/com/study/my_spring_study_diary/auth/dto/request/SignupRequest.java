package com.study.my_spring_study_diary.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원가입 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 3, max = 50, message = "사용자 이름은 3자에서 50자 사이여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "아이디는 영문, 숫자, 언더스코어만 가능합니다.")
    private String username;

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자를 초과해서는 안 됩니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 100, message = "비밀번호는 6자에서 100자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[@$!%*#?&])[A-Za-z\\\\d@$!%*#?&]+$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @Size(max = 100, message = "전체 이름은 100자를 초과할 수 없습니다.")
    private String fullname;
}
