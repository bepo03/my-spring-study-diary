package com.study.my_spring_study_diary.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyLogDeleteResponse {
    private String message;
    private Long deletedId;

    public static StudyLogDeleteResponse of(Long id) {
        return StudyLogDeleteResponse.builder()
                .message("학습 일지가 성공적으로 삭제되었습니다.")
                .deletedId(id)
                .build();
    }
}
