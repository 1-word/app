package com.numo.api.domain.dailySentence.repository;

import com.numo.domain.sentence.WordDailySentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordDailySentenceRepository extends JpaRepository<WordDailySentence, Long> {
    @Transactional
    @Modifying
    @Query(value = "delete from word_daily_sentence " +
                    "where word_id in :wordIds",
            nativeQuery = true)
    void deleteByWord_WordIdIn(@Param("wordIds") List<Long> wordIds);
}
