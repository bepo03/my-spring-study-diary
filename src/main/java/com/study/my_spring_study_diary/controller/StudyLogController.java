package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.global.common.ApiResponse;
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

    // 생성자 주입
    // Spring이 StudyLogService Bean을 찾아서 자동으로 주입
    private StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    /**
     * 학습 일지 생성 (CREATE)
     * @PostMapping: POST 요청을 처리
     * @RequestBody: HTTP Body의 JSON을 객체로 변환
     *
     * POST /api/v1/logs
     */
    @PostMapping
    public StudyLogResponse createStudyLog(
            @RequestBody
            StudyLogCreateRequest request
    ) {
        return studyLogService.createStudyLog(request);
    }

    /**
     * 모든 학습 일지 조회 (READ - ALL)
     * @GetMapping: GET 요청을 처리
     *
     * GET /api/v1/logs
     */
    @GetMapping
    public List<StudyLogResponse> getAllStudyLogs() {
        // Service 호출하여 모든 학습 일지 조회
        return studyLogService.getAllStudyLogs();
    }

    // ID로 학습 일지 단건 조회
    @GetMapping("/{id}")
    public StudyLogResponse getStudyLogById(
            @PathVariable
            Long id
    ) {
        return studyLogService.getStudyLogById(id);
    }

    // Date 기준 조회
    @GetMapping("/date/{date}")
    public List<StudyLogResponse> getStudyLogsByDate(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return studyLogService.getStudyLogsByDate(date);
    }

    // 카테고리 기준 조회
    @GetMapping("/category/{categoryName}")
    public List<StudyLogResponse> getStudyLogsByCategory(
            @PathVariable
            String categoryName
    ) {
        return studyLogService.getStudyLogsByCategory(categoryName);
    }
}
