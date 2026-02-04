package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.StudyLog;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StudyLogRepository {

    private final Map<Long, StudyLog> database = new HashMap<>();

    private final AtomicLong sequence = new AtomicLong(1);

    public StudyLog save(StudyLog studyLog) {
        if (studyLog.getId() == null) {
            studyLog.setId(sequence.getAndIncrement());
        }

        database.put(studyLog.getId(), studyLog);

        return studyLog;
    }
}
