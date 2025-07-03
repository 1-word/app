package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class WordItemWriter implements ItemWriter<Word> {
    private final WordBatchJdbcRepository wordBatchJdbcRepository;
    private final Long userId;
    private final Long targetWordBookId;
    private final WordCopyService wordCopyService;

    public WordItemWriter(WordBatchJdbcRepository wordBatchJdbcRepository, Long userId, Long targetWordBookId) {
        this.wordBatchJdbcRepository = wordBatchJdbcRepository;
        this.userId = userId;
        this.targetWordBookId = targetWordBookId;
        this.wordCopyService = new WordCopyService(userId, targetWordBookId);
    }

    @Override
    public void write(Chunk<? extends Word> chunk) throws Exception {
        List<Word> words = (List<Word>) chunk.getItems();

        long startWordId = getMaxWordId() + 1;
        long startWordDetailId = getMaxWordDetailId() + 1;

        log.info("startWordId: {}", startWordId);
        log.info("startWordDetailId: {}", startWordDetailId);

        // id 중복이 발생하지 않도록 id 점유
        wordBatchJdbcRepository.updateWordAutoIncrementId(startWordId + words.size() + 1);
        wordBatchJdbcRepository.updateWordDetailAutoIncrementId(startWordDetailId + getWordDetailsSize() + 1);

        List<Word> copyWord = getCopyWord(startWordId, startWordDetailId, words);

        try {
            wordBatchJdbcRepository.batchInsert(copyWord);
        } catch (Exception e) {
            // id 점유 롤백
            wordBatchJdbcRepository.updateWordAutoIncrementId(startWordId);
            wordBatchJdbcRepository.updateWordDetailAutoIncrementId(startWordDetailId);
            log.info("id rollback, startWordId: {}, startWordDetailId: {}", startWordId, startWordDetailId, e);
            throw e;
        }
    }

    public List<Word> getCopyWord(long startWordId, long startWordDetailId, List<Word> originWords) {
        wordCopyService.setCurrentWordDetailId(startWordDetailId);
        return wordCopyService.getCopyWords(startWordId, originWords);
    }

    private Long getMaxWordId() {
        return wordBatchJdbcRepository.getMaxWordId();
    }

    private Long getMaxWordDetailId() {
        return wordBatchJdbcRepository.getMaxWordDetailId();
    }

    private int getWordDetailsSize() {
        return wordCopyService.getWordDetailSize();
    }
}
