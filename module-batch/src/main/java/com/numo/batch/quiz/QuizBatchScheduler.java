package com.numo.batch.quiz;

import com.numo.domain.base.Timestamped;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class QuizBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    public QuizBatchScheduler(JobLauncher jobLauncher, @Qualifier("quizJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    /**
     * 매일 자정마다 quiz 데이터 삭제하는 배치 작업 실행
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void runJob() {
        String time = Timestamped.getTimeString(LocalDateTime.now());
        try {
            jobLauncher.run(job,
                    new JobParametersBuilder()
                            .addString("time", time)
                            .toJobParameters()
            );
        } catch (JobRestartException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException e) {
            log.info("{} batch already running or complete", time);
        }
    }
}
