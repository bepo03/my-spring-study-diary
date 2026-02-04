package com.study.my_spring_study_diary.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StudyLog {

    private Long id;
    private String title;
    private String content;
    private Category category;
    private Understanding understanding;
    private Integer studyTime;
    private LocalDate studyDate;
    private LocalDateTime createdAt;
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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Category getCategory() {
        return category;
    }

    public Understanding getUnderstanding() {
        return understanding;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setUnderstanding(Understanding understanding) {
        this.understanding = understanding;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
