package com.study.my_spring_study_diary.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 학습 일지 수정 요청 DTO
 *
 * CREATE와 달리 모든 필드가 선택적입니다.
 * null이면 기존 값을 유지합니다.
 */
public class StudyLogUpdateRequest {
    @Getter
    @Setter
    private String title;           // null이면 기존 값 유지
    @Getter
    @Setter
    private String content;         // null이면 기존 값 유지
    @Getter
    @Setter
    private String category;        // null이면 기존 값 유지
    @Getter
    @Setter
    private String understanding;   // null이면 기존 값 유지
    @Getter
    @Setter
    private Integer studyTime;      // null이면 기존 값 유지
    @Getter
    @Setter
    private LocalDate studyDate;    // null이면 기존 값 유지

    // 기본 생성자
    public StudyLogUpdateRequest() {
    }

    /**
     * 모든 필드가 null인지 확인
     * 아무것도 수정할 내용이 없는 경우 체크용
     */
    public boolean hashNoUpdates() {
        return title == null
                && content == null
                && category == null
                && understanding == null
                && studyTime == null
                && studyDate == null;
    }
}
