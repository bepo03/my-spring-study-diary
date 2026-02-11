package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MySQL 기반 StudyLog DAO 구현
 * <p>
 * JdbcTemplate 사용:
 * - Spring에서 제공하는 JDBC 헬퍼 클래스다.
 * - Connection, Statement 등을 자동으로 관리한다.
 * - SQL 예외를 Spring의 DataAccessException으로 변환한다.
 */
@Repository
public class MySQLStudyLogDaoImpl implements StudyLogDao {
    private final JdbcTemplate jdbcTemplate;

    public MySQLStudyLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ==================== CREATE ====================

    @Override
    public StudyLog save(StudyLog studyLog) {
        String sql = """
                INSERT INTO study_logs (title, content, category, understanding, study_time, study_date)
                VALUE (?, ?, ?, ?, ?, ?)
                """;

        // KeyHolder: 자동 생성된 ID를 수신하는 객체
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, studyLog.getTitle());
            ps.setString(2, studyLog.getContent());
            ps.setString(3, studyLog.getCategory().name());
            ps.setString(4, studyLog.getUnderstanding().name());
            ps.setInt(5, studyLog.getStudyTime());
            ps.setDate(6, Date.valueOf(studyLog.getStudyDate()));
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            studyLog.setId(generatedId.longValue());
        }

        return studyLog;
    }

    // ==================== READ ====================

    @Override
    public Optional<StudyLog> findById(Long id) {
        String sql = "SELECT * FROM study_logs WHERE id = ?";

        try {
            StudyLog studyLog = jdbcTemplate.queryForObject(sql, studyLogRowMapper, id);
            return Optional.ofNullable(studyLog);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<StudyLog> findAll() {
        String sql = "SELECT * FROM study_logs ORDER BY study_date DESC, id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper);
    }

    @Override
    public List<StudyLog> findByCategory(Category category) {
        String sql = "SELECT * FROM study_logs WHERE category = ? ORDER BY study_date DESC, id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper, category);
    }

    @Override
    public List<StudyLog> findByStudyDate(LocalDate date) {
        String sql = "SELECT * FROM study_logs WHERE study_date = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper, Date.valueOf(date));
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM study_logs WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM study_logs";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    // ==================== UPDATE ====================

    @Override
    public StudyLog update(StudyLog studyLog) {
        String sql = """
                UPDATE study_logs
                SET title = ?, content = ?, category =?, understanding = ?,
                    study_time = ?, study_date = ?
                WHERE id = ?
                """;

        int updated = jdbcTemplate.update(sql,
                studyLog.getTitle(),
                studyLog.getContent(),
                studyLog.getCategory().name(),
                studyLog.getUnderstanding().name(),
                studyLog.getStudyTime(),
                studyLog.getStudyDate(),
                studyLog.getId()
                );

        if (updated == 0) {
          throw new RuntimeException("학습 일지를 찾을 수 없습니다. ID: " + studyLog.getId());
        }

        return studyLog;
    }

    // ==================== DELETE ====================

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM study_logs WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM study_logs";
        jdbcTemplate.update(sql);
    }

    // ==================== PRIVATE METHODS ====================

    /**
     * RowMapper: ResulSet의 각 행을 StudyLog 객체로 반환합니다.
     */
    private final RowMapper<StudyLog> studyLogRowMapper = (rs, rowMapper) -> {
        StudyLog studyLog = new StudyLog();
        studyLog.setId(rs.getLong("id"));
        studyLog.setTitle(rs.getString("title"));
        studyLog.setContent(rs.getString("content"));
        studyLog.setCategory(Category.valueOf(rs.getString("category")));
        studyLog.setUnderstanding(Understanding.valueOf(rs.getString("understanding")));
        studyLog.setStudyTime(rs.getInt("study_time"));
        studyLog.setStudyDate(rs.getDate("study_date").toLocalDate());
        return studyLog;
    };
}
