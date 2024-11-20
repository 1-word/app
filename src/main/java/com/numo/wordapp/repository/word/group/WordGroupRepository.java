package com.numo.wordapp.repository.word.group;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.entity.word.detail.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordGroupRepository extends JpaRepository<WordGroup, Long>, WordGroupCustomRepository {
    Optional<List<WordGroup>> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndName(Long userId, String name);

    default WordGroup findWordGroupByIdAndUser(Long wordGroupId, Long userId) {
        return findWordGroupByIdAndUserId(wordGroupId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}
