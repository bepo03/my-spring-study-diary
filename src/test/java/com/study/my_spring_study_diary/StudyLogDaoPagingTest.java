package com.study.my_spring_study_diary;

import com.study.my_spring_study_diary.common.Page;
import com.study.my_spring_study_diary.dao.StudyLogDao;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class StudyLogDaoPagingTest {

    @Autowired
    private StudyLogDao studyLogDao;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 25건 생성
        for (int i = 1; i <= 25; i++) {
            StudyLog log = new StudyLog();
            log.setTitle("학습 일지 " + i);
            log.setContent("내용 " + i);
            log.setCategory(
                    i % 3 == 0 ? Category.SPRING
                            : i % 3 == 1 ? Category.JAVA
                            : Category.DATABASE);
            log.setUnderstanding(Understanding.GOOD);
            log.setStudyTime(60 + i);
            log.setStudyDate(LocalDate.of(2024, 1, 1).plusDays(i - 1));
            log.setCreatedAt(LocalDateTime.now());
            log.setUpdatedAt(LocalDateTime.now());
            studyLogDao.save(log);
        }
    }

    @Test
    @DisplayName("첫 번째 페이지 조회 - 10건")
    void findAllWithPaging_firstPage() {
        // when
        Page<StudyLog> result = studyLogDao.findAllWithPaging(0, 10);

        // then
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(25);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.isFirst()).isTrue();
        assertThat(result.isLast()).isFalse();
        assertThat(result.isHasNext()).isTrue();
        assertThat(result.isHasPrevious()).isFalse();
    }

    @Test
    @DisplayName("마지막 페이지 조회 - 5건")
    void findAllWithPaging_lastPage() {
        // when
        Page<StudyLog> result = studyLogDao.findAllWithPaging(2, 10);

        // then
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getPage()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.isFirst()).isFalse();
        assertThat(result.isLast()).isTrue();
        assertThat(result.isHasNext()).isFalse();
        assertThat(result.isHasPrevious()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 페이지 조회 - 빈 결과")
    void findAllWithPaging_emptyPage() {
        // when
        Page<StudyLog> result = studyLogDao.findAllWithPaging(10, 10);

        // then
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("카테고리별 페이징 조회")
    void findByCategoryWithPaging() {
        // when
        Page<StudyLog> result = studyLogDao.findByCategoryWithPaging("SPRING", 0, 5);

        // then
        assertThat(result.getContent())
                .allMatch(log -> log.getCategory() == Category.SPRING);
        assertThat(result.getTotalElements()).isLessThanOrEqualTo(25);
    }

    @Test
    @DisplayName("검색 + 페이징 조회")
    void searchWithPaging() {
        // when
        Page<StudyLog> result = studyLogDao.searchWithPaging(
                "학습",             // 제목 키워드
                null,               // 카테고리 (전체)
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 15),
                0, 10);

        // then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent())
                .allMatch(log -> log.getTitle().contains("학습"));
        assertThat(result.getContent())
                .allMatch(log ->
                        !log.getStudyDate().isBefore(LocalDate.of(2024, 1, 1))
                                && !log.getStudyDate().isAfter(LocalDate.of(2024, 1, 15)));
    }
}