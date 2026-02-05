package com.study.my_spring_study_diary.global.exception;

import lombok.Getter;

public class InvalidPageRequestException extends RuntimeException {
    @Getter
    private final int requestedPage;
    @Getter
    private final int totalPages;

    public InvalidPageRequestException(int requestedPage, int totalPages) {
        super(String.format(
                "잘못된 페이지 요청입니다. 요청 페이지: %d, 전체 페이지: %d (0~%d)",
                requestedPage, totalPages, totalPages - 1
        ));
        this.requestedPage = requestedPage;
        this.totalPages = totalPages;
    }
}
