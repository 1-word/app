package com.numo.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class WordChunkListener implements ChunkListener {
    @Override
    public void afterChunk(ChunkContext context) {
        long readCount = context.getStepContext().getStepExecution().getReadCount();
        log.info("현재 까지의 read 수: {}", readCount);
    }
}
