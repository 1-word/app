package com.numo.api.repository.word.group;

import com.numo.domain.word.detail.WordGroup;

import java.util.List;
import java.util.Optional;

public interface WordGroupCustomRepository {
    List<WordGroup> findWordGroupsByUserId(Long userId);
    Optional<WordGroup> findWordGroupByIdAndUserId(Long wordGroupId, Long userId);
    boolean existsGroup(Long userId, String groupName);
}