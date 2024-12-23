package com.numo.wordapp.repository.sentence;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.repository.sentence.query.DailySentenceCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailySentenceRepository extends JpaRepository<DailySentence, Long>, DailySentenceCustomRepository {
    Optional<DailySentence> findByDailySentenceIdAndUser_UserId(Long dailySentenceId, Long userId);

    default DailySentence findDailySentenceBy(Long dailySentenceId, Long userId) {
        return findByDailySentenceIdAndUser_UserId(dailySentenceId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DAILY_SENTENCE_NOT_FOUND)
        );
    }
}
