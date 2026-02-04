package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class StudyLogRepository {

    private final Map<Long, StudyLog> database = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @PostConstruct
    public void init() {
        System.out.println("🚀 StudyLogRepository 초기화 완료!");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("🔚 StudyLogRepository 종료! 저장된 데이터: " + database.size() + "개");
    }

    // 학습 일지 저장
    public StudyLog save(StudyLog studyLog) {
        if (studyLog.getId() == null) {
            studyLog.setId(sequence.getAndIncrement());
        }

        database.put(studyLog.getId(), studyLog);

        return studyLog;
    }

    // 전체 학습 일지 조회 (최신순 정렬)
    public List<StudyLog> findAll() {
        return database.values().stream()
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                .collect(Collectors.toList());
    }

    // ID로 학습 일지 조회
    // @return Optional: 값이 있을 수도, 없을 수도 있음
    public Optional<StudyLog> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    // 날짜로 학습 일지 조회
    public List<StudyLog> findByStudyDate(LocalDate date) {
        return database.values().stream()
                .filter(log -> log.getStudyDate().equals(date))
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                .collect(Collectors.toList());
    }

    // 카테고리로 학습 일지 조회
    public List<StudyLog> findByCategory(Category category) {
        return database.values().stream()
                .filter(log -> log.getCategory().equals(category))
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                .collect(Collectors.toList());
    }

    // 저장된 데이터 개수 조회
    public long count() {
        return database.size();
    }
}
