package com.numo.api.domain.wordbook.repository;

import com.numo.api.domain.wordbook.dto.InWordCountDto;
import com.numo.api.domain.wordbook.dto.WordBookCountResponse;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.WordBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordBookRepository extends JpaRepository<WordBook, Long> {
    default WordBook findWordBookById(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    @Query(value = "SELECT " +
        "        wb1_0.word_book_id, " +
            "    u1_0.nickname, " +
            "    wb1_0.name, " +
            "    wb1_0.memo, " +
            "    wb1_0.color, " +
            "    wb1_0.background, " +
            "    COUNT(w.word_id) AS total_count, " +
            "    COUNT(CASE WHEN w.memorization = 'Y' THEN 1 END) AS memorized_count, " +
            "    COUNT(CASE WHEN w.memorization = 'N' THEN 1 END) AS un_memorized_count " +
            "FROM word_book wb1_0 JOIN `user` u1_0 " +
            "    ON u1_0.user_id=wb1_0.user_id " +
            "    LEFT JOIN word w ON wb1_0.word_book_id = w.word_book_id " +
            "WHERE wb1_0.user_id=:userId " +
            "GROUP BY wb1_0.word_book_id", nativeQuery = true)
    List<WordBookCountResponse>  findWordBooksCount(@Param("userId") Long userId);

    @Query(value = "SELECT " +
            "    wb.word_book_id, " +
            "    COUNT(w.word_id) AS total_count, " +
            "    COUNT(CASE WHEN w.memorization = 'Y' THEN 1 END) AS memorized_count, " +
            "    COUNT(CASE WHEN w.memorization = 'N' THEN 1 END) AS un_memorized_count " +
            "FROM word_book wb " +
            " join word w on wb.word_book_id = w.word_book_id " +
            "where 1=1 " +
            "    and wb.user_id = :userId " +
            "GROUP BY word_book_id", nativeQuery = true)
    List<InWordCountDto> findWordsCount(@Param("userId") Long userId);
}

