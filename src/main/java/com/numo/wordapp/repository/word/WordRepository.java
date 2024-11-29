package com.numo.wordapp.repository.word;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.word.ReadWordRequestDto;
import com.numo.wordapp.entity.word.Word;
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
