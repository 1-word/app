package com.numo.api.domain.wordbook.word.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.domain.wordbook.word.Word;
import com.numo.api.domain.wordbook.word.repository.query.WordCustomRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer>, WordCustomRepository {
    Optional<Word> findByUser_UserIdAndWordId(Long userId, Long wordId);

    default Word findByUserIdAndWordId(Long userId, Long wordId) {
        return findByUser_UserIdAndWordId(userId, wordId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    Slice<Word> findWordBy(Pageable pageable, ReadWordRequestDto readDto);
}
