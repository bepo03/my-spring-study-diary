-- 데이터베이스가 없으면 생성
CREATE DATABASE IF NOT EXISTS diary_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 데이터베이스 사용
USE diary_db;

-- 학습 일지 테이블 생성
CREATE TABLE IF NOT EXISTS study_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '학습 일지 ID',
    title VARCHAR(100) NOT NULL COMMENT '학습 주제',
    content TEXT NOT NULL COMMENT '학습 내용',
    category VARCHAR(50) NOT NULL COMMENT '카테고리 (JAVA, SPRING, DATABASE, ALGORITHM, ETC)',
    understanding VARCHAR(20) NOT NULL COMMENT '이해도 (VERY_GOOD, GOOD, NORMAL, BAD, VERY_BAD)',
    study_time INT NOT NULL COMMENT '학습 시간 (분)',
    study_date DATE NOT NULL COMMENT '학습 날짜',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '학습 일지 테이블';

-- 인덱스 생성
CREATE INDEX idx_study_logs_category ON study_logs(category);
CREATE INDEX idx_study_logs_study_date ON study_logs(study_date);
CREATE INDEX idx_study_logs_understanding ON study_logs(understanding);
CREATE INDEX idx_study_logs_created_at ON study_logs(created_at);
