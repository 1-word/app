package com.numo.wordapp.repository.word.group;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.entity.word.detail.WordGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordGroupRepository extends JpaRepository<WordGroup, Long>, WordGroupCustomRepository {
    boolean existsByDefaultGroup(String defaultGroup);

    boolean existsByWordGroupIdAndDefaultGroup(Long WordGroupId, String defaultGroup);

    default WordGroup findWordGroupByIdAndUser(Long wordGroupId, Long userId) {
        return findWordGroupByIdAndUserId(wordGroupId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }
}
