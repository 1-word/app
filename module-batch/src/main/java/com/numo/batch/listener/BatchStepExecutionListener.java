package com.numo.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class BatchStepExecutionListener implements StepExecutionListener {
    private final Class<?> cls;

    public BatchStepExecutionListener(Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("[{}] 작업 시작: {}", cls.getSimpleName(), stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getReadCount() == 0) {
            log.error("[{}] 읽을 데이터가 없어 종료합니다.", cls.getSimpleName());
            return ExitStatus.COMPLETED;
        }
        if (stepExecution.getStatus() == BatchStatus.FAILED) {
            log.error("[{}] 작업 중 오류가 발생했습니다 : {}", cls.getSimpleName(), stepExecution.getFailureExceptions());
            return ExitStatus.FAILED;
        }
        log.info("[{}] 읽은 데이터 수: {}", cls.getSimpleName(), stepExecution.getReadCount());
        return ExitStatus.COMPLETED;
    }
}
