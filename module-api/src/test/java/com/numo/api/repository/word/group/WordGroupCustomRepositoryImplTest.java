package com.numo.api.repository.word.group;

import com.numo.api.domain.wordbook.group.repository.WordGroupRepository;
import com.numo.api.domain.wordbook.group.dto.ReadWordGroupResponseDto;
import com.numo.api.domain.wordbook.group.dto.WordGroupResponseDto;
import com.numo.domain.word.detail.WordGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WordGroupCustomRepositoryImplTest {

    @Autowired
    WordGroupRepository wordGroupRepository;

    @Test
    void findWordGroupsByUserId() {
        Long userId = 2L;

        List<WordGroup> wordGroups = wordGroupRepository.findWordGroupsByUserId(userId);
        List<WordGroupResponseDto> res = wordGroups.stream().map(WordGroupResponseDto::of).toList();
        assertThat(res.size() > 0);
        System.out.println(res);
    }

    @Transactional
    @Test
    void findWordGroupByIdAndUserId() {
        Long userId = 2L;
        Long wordGroupId = 1L;

        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUserId(wordGroupId, userId)
                .orElse(WordGroup.builder().build());
        ReadWordGroupResponseDto res = ReadWordGroupResponseDto.of(wordGroup);
        assertThat(Objects.nonNull(res.wordGroup().wordGroupId()));
    }
}