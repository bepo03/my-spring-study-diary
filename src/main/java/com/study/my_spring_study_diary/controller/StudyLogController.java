package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.common.Page;
import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.request.StudyLogUpdateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogDeleteResponse;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.global.common.ApiResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 학습 일지 컨트롤러
 * <p>
 * {@code @RestController} 어노테이션 설명:
 * - {@code @Controller} + {@code @ResponseBody}의 조합
 * - 이 클래스의 모든 메서드 반환값을 JSON으로 변환하여 응답
 * - REST API 개발 시 사용
 *
 * {@code @RequestMapping} 어노테이션 설명:
 * - 이 컨트롤러의 기본 URL 경로를 설정
 * - 모든 메서드의 URL 앞에 "/api/v1/logs"가 붙음
 */
@RestController // REST API 컨트롤러로 등록!
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

    // ==================== CREATE ====================

    /**
     * 학습 일지 생성 (CREATE)
     * <p>
     * {@code @PostMapping}  POST 요청을 처리
     * {@code @RequestBody}  HTTP Body의 JSON을 객체로 변환
     * <p>
     * POST /api/v1/logs
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyLogResponse>> createStudyLog(
            @RequestBody
            StudyLogCreateRequest request
    ) {
        // Service 호출하여 학습 일지 생성
        StudyLogResponse response = studyLogService.createStudyLog(request);

        // 201 Created 상태 코드와 함께 응답
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // ==================== READ ====================

    /**
     * 모든 학습 일지 조회 (READ - ALL)
     * <p>
     * {@code @GetMapping} GET 요청을 처리
     * <p>
     * GET /api/v1/logs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getAllStudyLogs() {
        // Service 호출하여 모든 학습 일지 조회
        List<StudyLogResponse> responses = studyLogService.getAllStudyLogs();

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responses));

    }

    /**
     * 특정 학습 일지 조회 (READ - Single)
     * <p>
     * {@code @GetMapping("/{id}")} GET 요청을 처리 (꼉로 변수 포함)
     * {@code @PathVariable} URL 경로의 {id} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogResponse>> getStudyLogById(
            @PathVariable
            Long id
    ) {
        // Service 호출하여 ID로 학습 일지 조회
        StudyLogResponse response =  studyLogService.getStudyLogById(id);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response));
    }

    /**
     * 날짜별 학습 일지 조회 (READ - By Date)
     * <p>
     * {@code @GeMapping("/date/{date}")} GET 요청을 처리 (날짜 경로 변수 포함)
     * {@code @PathVariable} URL 경로의 {date} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/date/{date}
     * 예시: GET /api/v1/logs/date/2025-01-15
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getStudyLogsByDate(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        // Service 호출하여 날짜로 학습 일지 조회
        List<StudyLogResponse> responses = studyLogService.getStudyLogsByDate(date);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responses));

    }

    /**
     * 카테고리별 학습 일지 조회 (READ - By Category)
     * <p>
     * {@code @GetMapping("/category/{category}")} GET 요청을 처리 (카테코리 경로 변수 포함)
     * {@code @PathVariable} URL 경로의 {category} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/category/{category}
     * 예시: GET /api/v1/logs/category/SPRING
     *       GET /api/v1/logs/category/JAVA
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<StudyLogResponse>>> getStudyLogsByCategory(
            @PathVariable
            String category
    ) {
        // Service 호출하여 카테고리로 학습 일지 조회
        List<StudyLogResponse> responses = studyLogService.getStudyLogsByCategory(category);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(responses));
    }

    // ==================== PAGING ====================

    /**
     * 전체 학습 일지 페이징 조회
     * GET /api/v1/logs/page?page=0&size=10
     * GET /api/v1/logs/page -> 기본값 page=0, size=10
     *
     * @param page 페이지 번호 (0-based, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10, 최대값: 100)
     * @return 페이징된 학습 일지
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<StudyLogResponse>>> getStudyLogWithPaging(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {
        Page<StudyLogResponse> response = studyLogService.getStudyLogWithPaging(page, size);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response));
    }

    /**
     * 카테고리별 학습 일지 페이징 조회
     * GET /api/v1/logs/category/{category}/page?page=0&size=10
     *
     * @param category 카테고리
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 학습 일지
     */
    @GetMapping("/category/{category}/page")
    public ResponseEntity<ApiResponse<Page<StudyLogResponse>>> getStudyLogsByCategoryWithPaging(
            @PathVariable
            String category,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {
        Page<StudyLogResponse> response = studyLogService.getStudyLogsByCategoryWithPaging(category, page, size);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StudyLogResponse>>> searchStudyLogsWithPaging(
            @RequestParam(required = false)
            String title,
            @RequestParam(required = false)
            String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {
        Page<StudyLogResponse> response = studyLogService.searchStudyLogsWithPaging(
                title, category, startDate, endDate, page, size);

        // 200 OK 상태 코드와 함께 응답
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response));
    }

    // ==================== UPDATE ====================

    /**
     * 학습 일지 수정
     * PUT /api/v1/logs/{id}
     * <p>
     * {@code @PutMapping} PUT 요청을 처리하는 어노테이션
     *                     리소스의 전체 또는 일부를 수정할 때 사용
     * <p>
     * {@code @PathVariable} URL의 {id} 부분을 파라미터로 받음
     * {@code @RequestBody} HTTP Body의 JSON을 객체로 변환
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogResponse>> updateStudyLog(
            @PathVariable
            Long id,
            @RequestBody
            StudyLogUpdateRequest request
    ) {
        StudyLogResponse response = studyLogService.updateStudyLog(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ==================== DELETE ====================

    /**
     * 학습 일지 삭제 API
     * <p>
     * DELETE /api/v1/logs/{id}
     *
     * @param id 삭제할 학습 일지 ID
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyLogDeleteResponse>> deleteStudyLog(
            @PathVariable
            Long id
    ) {
        StudyLogDeleteResponse response = studyLogService.deleteStudyLog(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 전체 학습 일지 삭제 API
     *
     * @return 삭제 결과
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteAllStudyLogs() {
        Map<String, Object> response = studyLogService.deleteAllStudyLogs();
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
