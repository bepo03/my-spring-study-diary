package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.common.Page;
import com.study.my_spring_study_diary.dao.StudyLogDao;
import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.request.StudyLogUpdateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogDeleteResponse;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import com.study.my_spring_study_diary.global.exception.StudyLogNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 학습 일지 서비스
 * <p>
 * DIP(Dependency Inversion Principle) 적용:
 * - Service(고수준)가 구체적인 Repository(저수준)에 의존하지 않음
 * - StudyLogDao 인터페이스(추상화)에만 의존
 * - 구현체(MapStudyLogRepository, MySQLStudyLogDaoImpl 등)는 언제든 교체 가능
 * <p>
 * {@code @Service} 어노테이션 설명:
 * - 이 클래스를 Spring Bean으로 등록합니다
 * - 비즈니스 로직을 담당하는 서비스 계층임을 명시합니다
 * - {@code @Component}와 기능적으로 동일하지만, 역할을 명확히 표현합니다.
 */
@Service    // Spring Bean으로 등록!
public class StudyLogService {

    // DIP 준수: 인터페이스에만 의존
    private final StudyLogDao studyLogDao;

    // 페이징 관련 상수
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    /**
     * 생성자 주입 (Constructor Injection)
     * <p>
     * Spring이 StudyLogDao 인터페이스의 구현체를 찾아서 자동으로 주입
     * 현재는 MapStudyLogRepository가 주입됨
     * 향후 MySQLStudyLogDaoImpl 등올 쉽게 교체 가능
     */
    public StudyLogService(StudyLogDao studyLogDao) {
        this.studyLogDao = studyLogDao;
    }

    // ==================== CREATE ====================

    /**
     * 학습 일지 생성
     *
     * @param request 생성 요청 DTO
     * @return 생성된 학습 일지 응답 DTO
     */
    public StudyLogResponse createStudyLog(StudyLogCreateRequest request) {
        // 1. 요청 데이터 유효성 검증
        validateCreateRequest(request);

        // 2. DTO -> Entity 변환
        StudyLog studyLog = new StudyLog(
                null,   // ID는 Repository에서 자동 생성
                request.getTitle(),
                request.getContent(),
                Category.valueOf(request.getCategory()),
                Understanding.valueOf(request.getUnderstanding()),
                request.getStudyTime(),
                request.getStudyDate() != null ? request.getStudyDate() : LocalDate.now()
        );

        // 3. 저장
        StudyLog savedStudyLog = studyLogDao.save(studyLog);

        // 4. Entity -> Response DTO 변환 후 반환
        return StudyLogResponse.from(savedStudyLog);
    }

    // ==================== READ ====================

    /**
     * 전체 학습 일지 목록 조회
     *
     * @return 모든 학습 일지 응답 DTO 리스트
     */
    public List<StudyLogResponse> getAllStudyLogs() {
        // 1. Repository에서 모든 학습 일지 조회
        List<StudyLog> studyLogs = studyLogDao.findAll();

        // 2. Entity 리스트 -> Response DTO 리스트로 변환
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * ID로 학습 일지 단건 조회
     *
     * @param id 조회할 학습 일지 ID
     * @return 학습 일지 응답 DTO
     */
    public StudyLogResponse getStudyLogById(Long id) {
        // 1. Repository에서 ID로 조회
        // 2. 존재 하지 않으면 예외 처리
        StudyLog studyLog = studyLogDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 학습 일지를 찾을 수 없습니다. (id: " + id + ")"
                ));
        // 2. Entity -> Response DTO 변환 후 반환
        return StudyLogResponse.from(studyLog);
    }

    // 날짜별 학습 일지 조회
    public List<StudyLogResponse> getStudyLogsByDate(LocalDate date) {
        List<StudyLog> studyLogs = studyLogDao.findByStudyDate(date);

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

        List<StudyLog> studyLogs = studyLogDao.findByCategory(category);

        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 처리된 학습 일지 목록 조회
     * 주의: 현재는 DB가 페이징을 직접 지원하지 않으므로 메모리에서 처리
     * TODO: 시제 프로덕션에서는 DB 레벨 페이징 구현 필요
     */
    public PageResponse<StudyLogResponse> getStudyLogWithPaging(PageRequest pageRequest) {
        // 1. 전체 데이터 조회
        List<StudyLog> allLogs = studyLogDao.findAll();

        // 2. 정렬 처리
        allLogs.sort((a, b) -> {
            int result = switch (pageRequest.getSortBy()) {
                case "title" -> a.getTitle().compareTo(b.getTitle());
                case "studyTime" -> a.getStudyTime().compareTo(b.getStudyTime());
                case "studyDate" -> a.getStudyDate().compareTo(b.getStudyDate());
                default -> a.getCreatedAt().compareTo(b.getCreatedAt());
            };
            return "ASC".equals(pageRequest.getSortDirection()) ? result : - result;
        });

        // 3. 페이징 처리
        long totalElements = allLogs.size();
        int start = pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), allLogs.size());

        List<StudyLog> pagedLogs = allLogs.subList(start, end);

        // 4. DTO 변환
        List<StudyLogResponse> responses = pagedLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        // 5. PageResponse 생성
        return PageResponse.of(
                responses,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements
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

        // 카테고리로 필터링
        List<StudyLog> filteredLogs = studyLogDao.findByCategory(category);

        // 정렬
        filteredLogs.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        // 페이징 처리
        long totalElements = filteredLogs.size();
        int start = pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), filteredLogs.size());

        List<StudyLog> pagedLogs = filteredLogs.subList(start, end);

        // DTO 변환
        List<StudyLogResponse> responses = pagedLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        return PageResponse.of(
                responses,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements
        );
    }

    // ==================== UPDATE ====================

    /**
     * 학습 일지 수정
     *
     * @param id 수정할 학습 일지 id
     * @param request 수정 요청 데이터
     * @return 수정된 학습 일지 응답
     */
    public StudyLogResponse updateStudyLog(Long id, StudyLogUpdateRequest request) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(request);

        // 1. 기존 학습 일지 조회
        StudyLog studyLog = studyLogDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다. (id: " + id + ")"));

        // 2. 수정할 내용이 있는지 확인
        if (request.hashNoUpdates()) {
            throw new IllegalArgumentException("수정할 내용이 없습니다.");
        }

        // 3. 수정할 값들의 유효성 검증
        validateUpdateRequest(request);

        // 4. 카테고리와 이해도 변환 (null이 아닌 경우에만)
        Category category = null;
        if (request.getCategory() != null) {
            try {
                category = Category.valueOf(request.getCategory().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + request.getCategory());
            }
        }

        Understanding understanding = null;
        if (request.getUnderstanding() != null) {
            try {
                understanding = Understanding.valueOf(request.getUnderstanding().toUpperCase());
            } catch (IllegalArgumentException e){
                throw new IllegalArgumentException("유효하지 않은 이해도입니다: " + request.getUnderstanding());
            }
        }

        // 5. Entity 업데이트 (null이 아닌 값만 반영)
        studyLog.update(
                request.getTitle(),
                request.getContent(),
                category,
                understanding,
                request.getStudyTime(),
                request.getStudyDate()
        );

        // 6. 저장 및 응답 반환
        StudyLog updatedStudyLog = studyLogDao.update(studyLog);
        return StudyLogResponse.from(updatedStudyLog);
    }

    // ==================== Validation ====================

    /**
     * 생성 요청 유효성 검증
     */
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

    /**
     * 수정 요청 유효성 검증
     * null이 아닌 값만 검증합니다.
     */
    public void validateUpdateRequest(StudyLogUpdateRequest request) {
        if (request.getTitle() != null) {
            if (request.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("학습 주제는 빈 값일 수 없습니다.");
            }
            if (request.getTitle().length() > 100) {
                throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다.");
            }
        }

        if (request.getContent() != null) {
            if (request.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("학습 내용은 빈 값일 수 없습니다.");
            }
            if (request.getContent().length() > 1_000) {
                throw new IllegalArgumentException("학습 내용은 1,000자를 초과할 수 없습니다.");
            }
        }

        if (request.getStudyTime() != null && request.getStudyTime() < 1) {
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야 합니다.");
        }

        if (request.getStudyDate() != null && request.getStudyDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("학습 날짜는 미래일 수 없습니다.");
        }
    }

    // ==================== DELETE ====================

    /**
     * 학습 일지를 삭제합니다.
     *
     * @param id 삭제할 학습 일지 ID
     * @return 삭제 결과 응답
     * @throws StudyLogNotFoundException 해당 ID의 학습 일지가 없는 경우
     */
    public StudyLogDeleteResponse deleteStudyLog(Long id) {
        // 1. 존재 여부 확인
        if (!studyLogDao.existsById(id)) {
            throw new StudyLogNotFoundException(id);
        }

        // 2. 삭제 수행
        boolean isDeleted = studyLogDao.deleteById(id);

        // 3. 삭제 결과 반환
        return StudyLogDeleteResponse.of(id);
    }

    /**
     * 전체 학습 일지를 삭제합니다.
     *
     * @return 삭제 결과 응답
     */
    public Map<String, Object> deleteAllStudyLogs() {
        long deletedCount = studyLogDao.count();
        studyLogDao.deleteAll();
        return Map.of(
                "message", "전체 학습 일지가 성공적으로 삭제되었습니다.",
                "deletedCount", deletedCount
        );
    }

    /**
     * 학습 일지 총 개수를 반환합니다.
     *
     * @return 학습 일지 총 개수
     */
    public long getStudyLogCount() {
        return studyLogDao.count();
    }

    // ==================== PAGING ====================

    /**
     * 전체 학습 일지 페이징 조회
     *
     * @param page 페이지 번호 (0-based)
     * @param size 페이지 크기
     * @return 페이징된 학습 일지 응답
     */
    public Page<StudyLogResponse> getStudyLogWithPaging(int page, int size) {
        // 파라미터 유효성 검증
        page = Math.max(0, page);   // 음수 방지
        size = Math.min(Math.max(1, size), MAX_PAGE_SIZE);  // 1 ~ 100범위

        // DAO에서 페이징된 Entity 조회
        Page<StudyLog> studyLogPage = studyLogDao.findAllWithPaging(page, size);

        // Entity -> DTO 변환
        List<StudyLogResponse> content = studyLogPage.getContent().stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        // Page<Entity>를 Page<DTO)로 변환하여 반환
        return new Page<>(content, page, size, studyLogPage.getTotalElements());
    }

    /**
     * 카테고리별 학습 일지 페이징 조회
     *
     * @param categoryStr 카테고리 문자열
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이징된 학습 일지 응답
     */
    public Page<StudyLogResponse> getStudyLogsByCategoryWithPaging(String categoryStr, int page, int size) {
        // 파라미터 유효성 검증
        page = Math.max(0, page);   // 음수 방지
        size = Math.min(Math.max(1, size), MAX_PAGE_SIZE);  // 1 ~ 100범위

        // 카테고리 유효성 검증
        if (categoryStr == null || categoryStr.isBlank()) {
            return new Page<>(List.of(), page, size, 0);
        }

        // DAO에서 페이징된 Entity 조회
        Page<StudyLog> studyLogPage = studyLogDao.findByCategoryWithPaging(categoryStr.toUpperCase(), page, size);

        // Entity -> DTO 변환
        List<StudyLogResponse> content = studyLogPage.getContent().stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        // Page<Entity>를 Page<DTO)로 변환하여 반환
        return new Page<>(content, page, size, studyLogPage.getTotalElements());
    }

    public Page<StudyLogResponse> searchStudyLogsWithPaging(
            String titleKeyword,
            String categoryStr,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    ) {
        // 파라미터 유효성 검증
        page = Math.max(0, page);   // 음수 방지
        size = Math.min(Math.max(1, size), MAX_PAGE_SIZE);  // 1 ~ 100범위

        // 카테고리 문자열을 대문자로 변환 (유효성 검증은 DAO에서 처리)
        String category = null;
        if (categoryStr != null && !categoryStr.isBlank()) {
            category = categoryStr.toUpperCase();
        }

        // DAO에서 페이징된 Entity 조회
        Page<StudyLog> studyLogPage = studyLogDao.searchWithPaging(
                titleKeyword, category, startDate, endDate, page, size);

        // Entity -> DTO 변환
        List<StudyLogResponse> content = studyLogPage.getContent().stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        // Page<Entity>를 Page<DTO)로 변환하여 반환
        return new Page<>(content, page, size, studyLogPage.getTotalElements());
    }
}
