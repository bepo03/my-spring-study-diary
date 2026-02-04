package com.study.my_spring_study_diary.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudyLogCreateRequest {

    private String title;
    private String content;
    private String category;
    private String understanding;
    private Integer studyTime;
    private LocalDate studyDate;

    public StudyLogCreateRequest() {
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

    public String getUnderstanding() {
        return understanding;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnderstanding(String understanding) {
        this.understanding = understanding;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }
}
