package com.study.my_spring_study_diary.global.exception;

public class StudyLogNotFoundException extends RuntimeException {
    public StudyLogNotFoundException(Long id) {
        super(String.format("해당 학습 일지를 찾을 수 없습니다. (id: %d)", id));
    }
}
