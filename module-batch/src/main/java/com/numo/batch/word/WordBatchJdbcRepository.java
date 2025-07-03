package com.numo.batch.word;

import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.word.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WordBatchJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Word> words) {
        insertWords(words);
        List<WordDetail> wordDetails = words.stream()
                .flatMap(w -> w.getWordDetails().stream())
                .toList();
        insertWordDetails(wordDetails);
    }

    private void insertWordDetails(List<WordDetail> wordDetails) {
        String sql = "INSERT INTO word_detail(" +
                "word_detail_id, word_id, word_group_id, title, content" +
                ") VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                                 wordDetails,
                                 wordDetails.size(),
                                 (ps, wordDetail) -> {
                                     ps.setLong(1, wordDetail.getWordDetailId());
                                     ps.setLong(2, wordDetail.getWordId());
                                     ps.setLong(3, wordDetail.getWordGroup().getWordGroupId());
                                     ps.setString(4, wordDetail.getTitle());
                                     ps.setString(5, wordDetail.getContent());
                                 });
    }

    private void insertWords(List<Word> words) {
        String sql = "INSERT INTO word(" +
                "word_id, word, word_book_id, user_id, lang, mean, memo, memorization, `read`" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                                 words,
                                 words.size(),
                                 (ps, word) -> {
                                     ps.setLong(1, word.getWordId());
                                     ps.setString(2, word.getWord());
                                     ps.setLong(3, word.getWordBookId());
                                     ps.setLong(4, word.getUser().getUserId());
                                     ps.setString(5, word.getLang() == null? GttsCode.EN.name() : word.getLang().name());
                                     ps.setString(6, word.getMean());
                                     ps.setString(7, word.getMemo());
                                     ps.setString(8, word.getMemorization());
                                     ps.setString(9, word.getRead());
                                 });
    }

    public Long getMaxWordId() {
        String sql = "SELECT MAX(word_id) FROM word";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long getMaxWordDetailId() {
        String sql = "SELECT MAX(word_detail_id) FROM word_detail";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public void updateWordDetailAutoIncrementId(Long endId) {
        checkId(endId);
        updateAutoIncrementId("word_detail", endId);
    }

    public void updateWordAutoIncrementId(Long endId) {
        checkId(endId);
        updateAutoIncrementId("word", endId);
    }

    private void updateAutoIncrementId(String tableName, Long id) {
        String sql = "ALTER TABLE " + tableName + " AUTO_INCREMENT = " + id;
        jdbcTemplate.execute(sql);
    }

    private void checkId(Long endId) {
        if (endId == null || endId <= 0L) {
            throw new IllegalArgumentException("startId must be greater than 0");
        }
    }
}
