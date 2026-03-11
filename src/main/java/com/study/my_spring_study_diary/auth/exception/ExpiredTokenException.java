package com.study.my_spring_study_diary.auth.exception;

/**
 * JWT 토큰 만료 예외
 */
public class ExpiredTokenException extends AuthException {

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
