package com.study.my_spring_study_diary.auth.exception;

/**
 * 인증 관련 예외
 */
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String mesage, Throwable cause) {
        super(mesage, cause);
    }
}
