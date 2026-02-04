package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudyLogService {

    private final StudyLogRepository studyLogRepository;

    public StudyLogService(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }

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
