package com.numo.batch.quiz;

import com.numo.domain.base.Timestamped;
import com.numo.domain.quiz.repository.QuizJdbcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Configuration
@Slf4j
public class QuizBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final QuizJdbcRepository quizJdbcRepository;

    public QuizBatch(JobRepository jobRepository, DataSource dataSource, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.quizJdbcRepository = new QuizJdbcRepository(dataSource);
    }

    @Bean
    public Job quizJob() {
        return new JobBuilder("quizJob", jobRepository)
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    quizJdbcRepository.deleteQuiz();
                    log.info("{} 퀴즈 데이터 삭제 완료", Timestamped.getTimeString(LocalDateTime.now()));
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
