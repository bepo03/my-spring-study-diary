package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.common.Page;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * StudyLog DAO 인터페이스
 * <p>
 * DAO(Data Access Object) 패턴:
 * - 데이터베이스 접근 로직을 캡슐화
 * - 비즈니스 로직과 데이터 접근 로직을 분리
 * - 데이터베이스 종류에 독립적인 인터페이스 제공
 */
public interface StudyLogDao {

    // ==================== CREATE ====================

    StudyLog save(StudyLog studyLog);

    // ==================== READ ====================

    Optional<StudyLog> findById(Long id);
    List<StudyLog> findAll();
    List<StudyLog> findByCategory(Category category);

    List<StudyLog> findByStudyDate(LocalDate date);

    boolean existsById(Long id);

    // ==================== UPDATE ====================

    StudyLog update(StudyLog studyLog);

    // ==================== DELETE ====================

    boolean deleteById(Long id);

    void deleteAll();

    // ==================== PAGING ====================

    /**
     * 전체 학습 일지를 페이징하여 조회
     *
     * @param page 페이지 번호 (0-based)
     * @param size 페이지당 데이터 개수
     * @return 페이징된 결과
     */
    Page<StudyLog> findAllWithPaging(int page, int size);

    /**
     * 카테고리별 학습 일지를 페이징하여 조회
     */
    Page<StudyLog> findByCategoryWithPaging(String category, int page, int size);

    /**
     * 검색 조건과 함께 페이징하여 조회
     * - 제목 키워드 검색
     * - 카테고리 필터
     * - 날짜 범위 필터
     */
    Page<StudyLog> searchWithPaging(
            String titleKeyword,
            String category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    );

    /**
     * 전체 데이터 개수 조회
     */
    long count();

    /**
     * 조건부 데이터 개수 조회
     */
    long countByCategory(String category);
}
