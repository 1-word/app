package com.numo.api.domain.wordbook.word.repository;

import com.numo.api.domain.wordbook.word.dto.WordCountDto;
import com.numo.api.domain.wordbook.word.repository.query.WordCustomRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long>, WordCustomRepository {
    List<Word> findByWordBook_id(Long wordBookId);

    default Word findByWordId(Long wordId) {
        return findById(wordId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    @Transactional
    @Modifying
    @Query(value = "delete from word " +
                    "where word_book_id = :wordBookId",
            nativeQuery = true)
    void deleteByWordBook_id(@Param("wordBookId") Long wordBookId);

    @Query(value = "SELECT new com.numo.api.domain.wordbook.word.dto.WordCountDto(" +
            "COUNT(*), " +
            "COUNT(CASE WHEN w.memorization = 'Y' THEN 1 END), " +
            "COUNT(CASE WHEN w.memorization = 'N' THEN 1 END)) " +
            "FROM Word w " +
            "where w.wordBook.id = :wordBookId")
    WordCountDto findCountByWordBookId(@Param("wordBookId") Long wordBookId);

}
