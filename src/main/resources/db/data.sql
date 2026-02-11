-- 초기 데이터 삽입 (테스트용)
INSERT INTO study_logs (title, content, category, understanding, study_time, study_date) VALUES
('Spring Boot 시작하기', 'Spring Boot 프로젝트 생성과 기본 설정을 학습했습니다.', 'SPRING', 'VERY_GOOD', 120, CURDATE()),
('Java Stream API', 'Stream API를 활용한 함수형 프로그래밍을 학습했습니다.', 'JAVA', 'GOOD', 90, DATE_SUB(CURDATE(), INTERVAL 1 DAY)),
('MySQL 인덱스 최적화', '데이터베이스 인덱스 설계와 최적화 방법을 학습했습니다.', 'DATABASE', 'NORMAL', 60, DATE_SUB(CURDATE(), INTERVAL 2 DAY));
