package com.numo.batch.word;

import com.numo.batch.listener.BatchStepExecutionListener;
import com.numo.batch.listener.WordItemWriteListener;
import com.numo.domain.wordbook.word.Word;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
@RequiredArgsConstructor
public class WordBatch {
    private static final int chunkSize = 500;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WordBatchRepository wordBatchRepository;
    private final DataShare<Long> dataShare;
    private final WordItemWriteListener wordItemWriteListener;

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
                .listener(new BatchStepExecutionListener(this.getClass()))
//                .listener((ItemWriteListener<Word>) wordItemWriteListener)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Word> wordReader(@Value("#{jobParameters['wordBookId']}") Long wordBookId) {
        return new RepositoryItemReaderBuilder<Word>()
                .name("wordReader")
                .methodName("findWordsBy")
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
