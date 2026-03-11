package com.study.my_spring_study_diary.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 권한 Enum
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER,
    ADMIN,
    MANAGER
}
