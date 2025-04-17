package com.numo.batch.word;

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
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class WordBatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    public WordBatchScheduler(JobLauncher jobLauncher, @Qualifier("wordCopyJob") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    /**
     * 단어 복사 job
     */
//    @Transactional
    public void runJob(Long userId, Long wordBookId, Long targetWordBookId) {
        String time = Timestamped.getTimeString(LocalDateTime.now());
        log.info("time: {}, start word copy batch", time);
        try {
            jobLauncher.run(job,
                    new JobParametersBuilder()
                            .addLong("userId", userId)
                            .addLong("wordBookId", wordBookId)
                            .addLong("targetWordBookId", targetWordBookId)
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters()
            );
        } catch (JobRestartException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException e) {
            log.info("{} batch already running or complete", time);
        }
    }
}
