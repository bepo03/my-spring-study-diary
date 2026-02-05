package com.study.my_spring_study_diary.global.common;

import lombok.Getter;
import lombok.Setter;

public class PageRequest {
    private static final int MAX_SIZE = 100;

    @Setter
    private int page = 0;                   // 페이지 번호 (0부터 시작)
    @Setter
    private int size = 10;                  // 페이지당 항목 수
    @Getter
    @Setter
    private String sortBy = "createdAt";    // 정렬 기준
    @Getter
    @Setter
    private String sortDirection = "DESC";  // 정렬 방향

    // 기본 생성자

    public PageRequest() {
    }

    // Getter/Setter

    public int getPage() {
        return Math.max(page, 0);
    }

    public int getSize() {
        return size <= 0 ? 10 : Math.min(size, MAX_SIZE);   // 최대 100개 제한
    }

    public int getOffset() {
        return page * size;
    }
}
