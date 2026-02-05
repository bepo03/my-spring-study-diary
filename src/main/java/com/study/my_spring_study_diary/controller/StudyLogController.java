package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.global.common.ApiResponse;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // REST API 컨트롤러로 등록
@RequestMapping("/api/v1/logs") // 기본 URL 경로 설정
public class StudyLogController {

    // 의존성 주입: Service를 주입받음
    private final StudyLogService studyLogService;

    /**
     * 생성자 주입
     * Spring이 StudyLogService Bean을 찾아서 자동으로 주입해줍니다.
     */
    private StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    /**
     * 학습 일지 생성 (CREATE)
     * {@code @PostMapping}  POST 요청을 처리
     * {@code @RequestBody}  HTTP Body의 JSON을 객체로 변환
     * POST /api/v1/logs
     */
    @PostMapping
    public StudyLogResponse createStudyLog(
            @RequestBody
            StudyLogCreateRequest request
    ) {
        // Service 호출하여 학습 일지 생성
        return studyLogService.createStudyLog(request);
    }

    /**
     * 모든 학습 일지 조회 (READ - ALL)
     * {@code @GetMapping} GET 요청을 처리
     * GET /api/v1/logs
     */
    @GetMapping
    public List<StudyLogResponse> getAllStudyLogs() {
        // Service 호출하여 모든 학습 일지 조회
        return studyLogService.getAllStudyLogs();
    }

    /**
     * 특정 학습 일지 조회 (READ - Single)
     * {@code @GetMapping("/{id}")} GET 요청을 처리 (꼉로 변수 포함)
     * {@code @PathVariable} URL 경로의 {id} 값을 매개변수로 받음
     * GET /api/v1/logs/{id}
     */
    @GetMapping("/{id}")
    public StudyLogResponse getStudyLogById(
            @PathVariable
            Long id
    ) {
        // Service 호출하여 ID로 학습 일지 조회
        return studyLogService.getStudyLogById(id);
    }

    /**
     * 날짜별 학습 일지 조회 (READ - By Date)
     * {@code @GeMapping("/date/{date}")} GET 요청을 처리 (날짜 경로 변수 포함)
     * {@code @PathVariable} URL 경로의 {date} 값을 매개변수로 받음
     * GET /api/v1/logs/date/{date}
     * 예시: GET /api/v1/logs/date/2025-01-15
     */
    @GetMapping("/date/{date}")
    public List<StudyLogResponse> getStudyLogsByDate(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        // Service 호출하여 날짜로 학습 일지 조회
        return studyLogService.getStudyLogsByDate(date);
    }

    /**
     * 카테고리별 학습 일지 조회 (READ - By Category)
     * @GetMapping("/category/{category}") GET 요청을 처리 (카테코리 경로 변수 포함)
     * @PathVariable URL 경로의 {category} 값을 매개변수로 받음
     * GET /api/v1/logs/category/{category}
     * 예시: GET /api/v1/logs/category/SPRING
     *       GET /api/v1/logs/category/JAVA
     */
    @GetMapping("/category/{category}")
    public List<StudyLogResponse> getStudyLogsByCategory(
            @PathVariable
            String category
    ) {
        // Service 호출하여 카테고리로 학습 일지 조회
        return studyLogService.getStudyLogsByCategory(category);
    }

    /**
     * 페이징 처리된 학습 일지 목록 조회
     * GET /api/v1/logs/page?page=0&size=1&sortBy=createdAt&sortDirection=DESC
     */
    @GetMapping("/page")
    public PageResponse<StudyLogResponse> getStudyLogWithPaging(
            @ModelAttribute
            PageRequest pageRequest
    ) {
        return studyLogService.getStudyLogWithPaging(pageRequest);
    }

    /**
     * 카테고리별 페이징 조회
     * GET /api/v1/logs/category/{category}/page?page=0&size=5
     */
    @GetMapping("/category/{category}/page")
    public PageResponse<StudyLogResponse> getStudyLogsByCategoryWithPaging(
            @PathVariable
            String category,
            @ModelAttribute
            PageRequest pageRequest
    ) {
        return studyLogService.getStudyLogsByCategoryWithPaging(category, pageRequest);
    }
}
