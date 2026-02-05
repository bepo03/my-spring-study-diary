package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    public StudyLogService(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }

    // 학습 일지 생성
    public StudyLogResponse createStudyLog(StudyLogCreateRequest request) {
        validateCreateRequest(request);

        StudyLog studyLog = new StudyLog(
                null,
                request.getTitle(),
                request.getContent(),
                Category.valueOf(request.getCategory()),
                Understanding.valueOf(request.getUnderstanding()),
                request.getStudyTime(),
                request.getStudyDate() != null ? request.getStudyDate() : LocalDate.now()
        );

        StudyLog savedStudyLog = studyLogRepository.save(studyLog);

        return StudyLogResponse.from(savedStudyLog);
    }

    // 전체 학습 일지 목록 조회
    public List<StudyLogResponse> getAllStudyLogs() {
        List<StudyLog> studyLogs = studyLogRepository.findAll();

        // Entity 리스트 -> Response DTO 리스트로 변환
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    // ID로 학습 일지 단건 조회
    public StudyLogResponse getStudyLogById(Long id) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 학습 일지를 찾을 수 없습니다. (id: " + id + ")"
                ));
        return StudyLogResponse.from(studyLog);
    }

    // 날짜별 학습 일지 조회
    public List<StudyLogResponse> getStudyLogsByDate(LocalDate date) {
        List<StudyLog> studyLogs = studyLogRepository.findByStudyDate(date);

        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    // 카테고리별 학습 일지 조회
    public List<StudyLogResponse> getStudyLogsByCategory(String categoryName) {
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "유효하지 않은 카테고리입니다: " + categoryName
            );
        }

        List<StudyLog> studyLogs = studyLogRepository.findByCategory(category);

        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 처리된 학습 일지 목록 조회
     */
    public PageResponse<StudyLogResponse> getStudyLogWithPaging(PageRequest pageRequest) {
        // Repository에서 페이징 처리된 데이터 조회
        PageResponse<StudyLog> pageResult = studyLogRepository.findAllWithPaging(pageRequest);

        // Entity를 Response DTO로 변환
        List<StudyLogResponse> responses = pageResult.getContent().stream()
                .map(StudyLogResponse::from)
                .toList();

        // 페이징 정보를 유지하면서 DTO로 변환
        return PageResponse.of(
                responses,
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getTotalElements()
        );
    }

    /**
     * 카테고리별 페이징 조회
     */
    public PageResponse<StudyLogResponse> getStudyLogsByCategoryWithPaging(
            String categoryName, PageRequest pageRequest
    ) {
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리: " + categoryName);
        }

        PageResponse<StudyLog> pageResult = studyLogRepository.findByCategoryWithPaging(category, pageRequest);

        List<StudyLogResponse> responses = pageResult.getContent().stream()
                .map(StudyLogResponse::from)
                .toList();

        return PageResponse.of(
                responses,
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getTotalElements()
        );
    }

    // 유효성 검증
    private void validateCreateRequest(StudyLogCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 주제는 필수입니다.");
        }

        if (request.getTitle().length() > 100) {
            throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다.");
        }

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 내용은 필수입니다.");
        }

        if (request.getContent().length() > 1_000) {
            throw new IllegalArgumentException("학습 내용은 1,000자를 초과할 수 없습니다.");
        }

        if (request.getStudyTime() == null || request.getStudyTime() < 1) {
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야 합니다.");
        }
    }
}
