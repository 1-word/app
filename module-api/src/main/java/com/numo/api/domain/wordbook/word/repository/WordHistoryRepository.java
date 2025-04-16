package com.numo.api.domain.wordbook.word.repository;

import com.numo.api.domain.wordbook.word.dto.WordHistoryDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.word.WordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordHistoryRepository extends JpaRepository<WordHistory, Long> {
    default WordHistory findWordHistoryById(Long targetWordHistoryId) {
        return findById(targetWordHistoryId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
    @Query("select new com.numo.api.domain.wordbook.word.dto.WordHistoryDto(" +
            "h.id, " +
            "h.operation, " +
            "h.beforeData, " +
            "h.afterData, " +
            "h.modifiedBy.nickname, " +
            "h.modifiedBy.profileImagePath, " +
            "h.createTime, " +
            "h.updateTime" +
            ") from WordHistory h " +
            "where h.wordBookId = :wordBookId")
    List<WordHistoryDto> findWordHistoryByWordBookId(@Param("wordBookId") Long wordBookId);
}
