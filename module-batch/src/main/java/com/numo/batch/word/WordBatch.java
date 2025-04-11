package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@Slf4j
public class WordBatch {
    private static final int chunkSize = 100;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WordBatchRepository wordBatchRepository;

    public WordBatch(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            WordBatchRepository wordBatchRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.wordBatchRepository = wordBatchRepository;
    }

    @Bean
    public Job wordCopyJob() {
        return new JobBuilder("wordCopyJob", jobRepository)
                .start(step())
                .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return new StepBuilder("wordCopyStep", jobRepository)
                .<Word, Word>chunk(chunkSize, platformTransactionManager)
                .reader(wordReader(null))
                .processor(wordProcessor(null, null))
                .writer(wordWriter())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        log.info("[word copy] 작업 시작: {}", stepExecution.getStepName());
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        if (stepExecution.getReadCount() == 0) {
                            log.info("[word copy] 복사할 데이터가 없어 종료합니다.");
                            return ExitStatus.COMPLETED;
                        }
                        if (stepExecution.getStatus() == BatchStatus.FAILED) {
                            log.error("[word copy] 작업 중 오류가 발생했습니다 : {}", stepExecution.getFailureExceptions());
                            return ExitStatus.FAILED;
                        }
                        log.info("[word copy] 현재 복사한 단어 수: {}", stepExecution.getReadCount());
                        return ExitStatus.COMPLETED;
                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Word> wordReader(@Value("#{jobParameters['wordBookId']}") Long wordBookId) {
        return new RepositoryItemReaderBuilder<Word>()
                .name("wordReader")
                .methodName("findByWordBook_Id")
                .arguments(wordBookId)
                .repository(wordBatchRepository)
                .pageSize(chunkSize)
                .sorts(Map.of("wordId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Word, Word> wordProcessor(
            @Value("#{jobParameters['userId']}") Long userId,
            @Value("#{jobParameters['targetWordBookId']}") Long targetWordBookId) {
        return word -> word.copyWithWordBook(userId, targetWordBookId);
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Word> wordWriter() {
        return new RepositoryItemWriterBuilder<Word>()
                .repository(wordBatchRepository)
                .build();
    }

}
