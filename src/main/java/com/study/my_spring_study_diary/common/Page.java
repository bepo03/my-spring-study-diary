package com.study.my_spring_study_diary.common;

import lombok.Getter;

import java.util.List;

/**
 * 페이징 응답 객체 (제네릭)
 *
 * @param <T> 페이지에 담길 데이터의 타입
 * <p>
 * 제네릭을 사용하는 이유:
 * - StudyLog 뿐 아니라 어떤 엔티티에도 재사용 가능
 * - 타입 안전성을 보장하여 컴파일 타입에 오류를 잡을 수 있음
 */
@Getter
public class Page<T> {
    private List<T> content;        // 현재 페이지의 데이터 목록
    private int page;               // 현재 페이지 번호 (0-based)
    private int size;               // 페이지당 데이터 개수
    private long totalElements;     // 전체 데이터 개수
    private int totalPages;         // 전체 페이지 수
    private boolean first;          // 첫 번째 페이지 여부
    private boolean last;           // 마지막 페이지 여부
    private boolean hasNext;        // 다음 페이지 존재 여부
    private boolean hasPrevious;    // 이전 페이지 존재 여부

    public Page(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;

        // 전체 페이지 수 계산
        // (총 데이터 수 + 페이지 크기 - 1) / 페이지 크기 -> 올림 나눗셈
        this.totalPages = (int) Math.ceil((double) totalElements / size);

        // 페이지 위치 정보 계산
        this.first = (page == 0);
        this.last = (page >= totalPages - 1) || (totalPages == 0);
        this.hasNext = !this.last;
        this.hasPrevious = !this.first;
    }
}
