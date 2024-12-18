package com.numo.wordapp.repository.word.group;

import com.numo.wordapp.dto.word.group.WordGroupReadResponseDto;
import com.numo.wordapp.dto.word.group.WordGroupResponseDto;
import com.numo.wordapp.entity.word.detail.WordGroup;
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
        WordGroupReadResponseDto res = WordGroupReadResponseDto.of(wordGroup);
        assertThat(Objects.nonNull(res.wordGroup().wordGroupId()));
    }
}