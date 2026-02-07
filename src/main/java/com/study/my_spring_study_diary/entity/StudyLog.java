package com.study.my_spring_study_diary.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudyLog {

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private Category category;
    @Getter
    @Setter
    private Understanding understanding;
    @Getter
    @Setter
    private Integer studyTime;
    @Getter
    @Setter
    private LocalDate studyDate;
    @Getter
    @Setter
    private LocalDateTime createdAt;
    @Getter
    @Setter
    private LocalDateTime updatedAt;

    public StudyLog() {
    }

    public StudyLog(Long id, String title, String content, Category category, Understanding understanding, Integer studyTime, LocalDate studyDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.understanding = understanding;
        this.studyTime = studyTime;
        this.studyDate = studyDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 학습 일지 정보 수정
     * <p>
     * null이 아닌 값만 업데이트 합니다.
     * 이 방식을 "Dirty Checking" 또는 "Partial Update" 라고 합니다.
     */
    public void update(String title, String content, Category category, Understanding understanding, Integer studyTime, LocalDate studyDate) {
        // null이 아닌 경우에만 업데이트
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
        if (category != null) {
            this.category = category;
        }
        if (understanding != null) {
            this.understanding = understanding;
        }
        if (studyTime != null) {
            this.studyTime = studyTime;
        }
        if (studyDate != null) {
            this.studyDate = studyDate;
        }

        // 수정 시간 갱신
        this.updatedAt = LocalDateTime.now();
    }
}
