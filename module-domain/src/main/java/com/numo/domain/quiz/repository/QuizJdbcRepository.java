package com.numo.domain.quiz.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class QuizJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public QuizJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 7일이 지난 퀴즈 데이터를 삭제한다
     */
    public void deleteQuiz() {
        String sql = "DELETE q FROM quiz q " +
                "INNER JOIN quiz_info qi ON q.quiz_info_id = qi.quiz_info_id " +
                "WHERE qi.create_time > NOW() - INTERVAL 7 DAY; ";
        jdbcTemplate.update(sql);
    }
}
