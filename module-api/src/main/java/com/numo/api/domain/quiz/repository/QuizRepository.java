package com.numo.api.domain.quiz.repository;

import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    boolean existsByQuizInfo_Id(Long quizInfoId);

    Optional<Quiz> findById(Long id);

    default Quiz findQuizBy(Long id) {
        return findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    Long countByQuizInfo_IdAndCorrectIsNull(Long quizInfoId);

    List<Quiz> findAllByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query(value = "insert into quiz (quiz_info_id, word_id) " +
            "select :quizInfoId as quiz_info_id, word_id " +
            "from word " +
            "where word_book_id = :wordBookId " +
            "and user_id = :userId " +
            "order by rand() " +
            "limit :limit",
            nativeQuery = true)
    void createQuizOrderByRandom(@Param("quizInfoId") Long quizInfoId,
                                 @Param("wordBookId") Long wordBookId,
                                 @Param("userId") Long userId,
                                 @Param("limit") int limit);

    @Transactional
    @Modifying
    @Query(value = "insert into quiz (quiz_info_id, word_id) " +
            "select :quizInfoId as quiz_info_id, word_id " +
            "from word " +
            "where word_book_id = :wordBookId " +
            "and user_id = :userId " +
            "limit :limit",
            nativeQuery = true)
    void createQuizOrderByCreated(@Param("quizInfoId") Long quizInfoId,
                                 @Param("wordBookId") Long wordBookId,
                                 @Param("userId") Long userId,
                                 @Param("limit") int limit);

    @Transactional
    @Modifying
    @Query(value = "insert into quiz (quiz_info_id, word_id) " +
            "select :quizInfoId as quiz_info_id, word_id " +
            "from word " +
            "where word_book_id = :wordBookId " +
            "and user_id = :userId " +
            "order by update_time desc " +
            "limit :limit",
            nativeQuery = true)
    void createQuizOrderByUpdated(@Param("quizInfoId") Long quizInfoId,
                                  @Param("wordBookId") Long wordBookId,
                                  @Param("userId") Long userId,
                                  @Param("limit") int limit);
}
