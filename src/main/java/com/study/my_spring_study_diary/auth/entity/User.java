package com.study.my_spring_study_diary.auth.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 유저 Entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password")
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;
    private String email;
    private String username;
    private String password;
    private UserRole role;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
