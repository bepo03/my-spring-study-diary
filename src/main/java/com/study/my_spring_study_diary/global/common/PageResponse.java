package com.study.my_spring_study_diary.global.common;

import lombok.Getter;

import java.util.List;

public class PageResponse<T> {

    @Getter
    private List<T> content;    // 실제 데이터
    @Getter
    private int pageNumber;     // 현재 페이지 번호
    @Getter
    private int pageSize;       // 페이지 크기
    @Getter
    private long totalElements; // 전체 데이터 개수
    @Getter
    private int totalPages;     // 전체 페이지 수
    @Getter
    private boolean first;      // 첫 페이지 여부
    @Getter
    private boolean last;       // 마지막 페이지 여부

    // 생성자

    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize) ;
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }

    public static <T> PageResponse<T> of(List<T> content, int pageNumber, int pageSize, long totalElements) {
        return new PageResponse<>(content, pageNumber, pageSize, totalElements);
    }

    // 다음 페이지 존재 여부
    public boolean hasNext() {
        return !last;
    }

    // 이전 페이지 존재 여부
    public boolean hasPrevious() {
        return !first;
    }
}
