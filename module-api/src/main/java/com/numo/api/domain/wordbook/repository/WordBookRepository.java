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

    @Query(value = "select " +
        "        wb1_0.word_book_id, " +
            "    u1_0.nickname, " +
            "    wb1_0.name, " +
            "    wb1_0.memo, " +
            "    wb1_0.color, " +
            "    wb1_0.background, " +
            "    b.total_count, " +
            "    b.memorized_count, " +
            "    b.un_memorized_count " +
            "from " +
            "    word_book wb1_0 " +
            "        join " +
            "    `user` u1_0 " +
            "    on u1_0.user_id=wb1_0.user_id " +
            "    LEFT JOIN ( " +
            "        SELECT " +
            "            word.word_book_id, " +
            "            COUNT(*) AS total_count, " +
            "            COUNT(CASE WHEN memorization = 'Y' THEN 1 END) AS memorized_count, " +
            "            COUNT(CASE WHEN memorization = 'N' THEN 1 END) AS un_memorized_count " +
            "        FROM word " +
            "        GROUP BY word.word_book_id " +
            "    ) b ON wb1_0.word_book_id = b.word_book_id " +
            "where " +
            "    wb1_0.user_id=:userId", nativeQuery = true)
    List<WordBookCountResponse>  findWordBooksCount(@Param("userId") Long userId);

    @Query(value = "SELECT " +
            "w.word_book_id, " +
            "COUNT(*) as total_count, " +
            "COUNT(CASE WHEN w.memorization = 'Y' THEN 1 END) as memorize, " +
            "COUNT(CASE WHEN w.memorization = 'N' THEN 1 END) as un_memorize " +
            "FROM word w " +
            "where w.user_id = :userId " +
            "group by w.word_book_id ", nativeQuery = true)
    List<InWordCountDto> findWordsCount(@Param("userId") Long userId);
}

