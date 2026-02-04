package com.study.my_spring_study_diary.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.study.my_spring_study_diary.entity.StudyLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id", "title", "content",
        "category", "categoryIcon",
        "understanding", "understandingEmoji",
        "studyTime", "studyDate",
        "createdAt", "updatedAt"
})
public class StudyLogResponse {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String categoryIcon;
    private String understanding;
    private String understandingEmoji;
    private Integer studyTime;
    private LocalDate studyDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public StudyLogResponse() {
    }

    public static StudyLogResponse from(StudyLog studyLog) {
        StudyLogResponse response = new StudyLogResponse();
        response.id = studyLog.getId();
        response.title = studyLog.getTitle();
        response.content = studyLog.getContent();
        response.category = studyLog.getCategory().name();
        response.categoryIcon = studyLog.getCategory().getIcon();
        response.understanding = studyLog.getUnderstanding().name();
        response.understandingEmoji = studyLog.getUnderstanding().getEmoji();
        response.studyTime = studyLog.getStudyTime();
        response.studyDate = studyLog.getStudyDate();
        response.createAt = studyLog.getCreateAt();
        response.updateAt = studyLog.getUpdateAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public String getUnderstanding() {
        return understanding;
    }

    public String getUnderstandingEmoji() {
        return understandingEmoji;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}
