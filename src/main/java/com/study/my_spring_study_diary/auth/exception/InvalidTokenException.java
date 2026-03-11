package com.study.my_spring_study_diary.auth.exception;

/**
 * JWT 토큰 유효히지 않음 예외
 */
public class InvalidTokenException extends AuthException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
