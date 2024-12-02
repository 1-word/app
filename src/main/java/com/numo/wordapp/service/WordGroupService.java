package com.numo.wordapp.service;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.word.group.WordGroupReadResponseDto;
import com.numo.wordapp.dto.word.group.WordGroupRequestDto;
import com.numo.wordapp.dto.word.group.WordGroupResponseDto;
import com.numo.wordapp.entity.word.detail.WordGroup;
import com.numo.wordapp.repository.word.group.WordGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordGroupService {
    private final WordGroupRepository wordGroupRepository;

    /**
     * 해당하는 유저의 품사 목록을 가져온다.
     * @param userId 유저 아이디
     * @return 품사 목록
     */
    public List<WordGroupResponseDto> getWordGroupList(Long userId) {
        List<WordGroup> wordGroups = wordGroupRepository.findWordGroupsByUserId(userId);
        List<WordGroupResponseDto> res = wordGroups.stream().map(WordGroupResponseDto::of).toList();
        return res;
    }

    /**
     * 해당하는 품사 아이디, 유저의 품사 데이터와 detail에 해당하는 데이터도 가져온다
     * @param userId 유저 아이디
     * @param wordGroupId 품사 아이디
     * @return 조회한 품사 데이터
     */
    @Transactional
    public WordGroupReadResponseDto getWordGroup(Long userId, Long wordGroupId) {
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        return WordGroupReadResponseDto.of(wordGroup);
    }

    /**
     * 품사 저장
     * @param userId 유저 아이디
     * @param requestDto 저장할 품사 데이터
     * @return 저장한 품사 데이터
     */
    public WordGroupResponseDto saveWordGroup(Long userId, WordGroupRequestDto requestDto) {
        // 동일한 품사명은 저장 불가
        checkGroup(userId, requestDto);
        WordGroup wordGroup = requestDto.toEntity(userId);
        return WordGroupResponseDto.of(wordGroupRepository.save(wordGroup));
    }

    /**
     * 품사 수정
     * @param userId 유저 아이디
     * @param wordGroupId 품사 아이디
     * @param requestDto 수정할 품사 데이터
     * @return 수정한 품사 데이터
     */
    @Transactional
    public WordGroupResponseDto updateWordGroup(Long userId, Long wordGroupId, WordGroupRequestDto requestDto) {
        // 동일한 품사명은 저장 불가
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        checkGroup(userId, requestDto);
        wordGroup.update(requestDto);
        return WordGroupResponseDto.of(wordGroup);
    }

    /**
     * 품사 삭제, 품사와 연관되어 있는 데이터 모두 삭제된다.
     * @param userId 유저 아이디
     * @param wordGroupId 품사 아이디
     */
    public void removeWordGroup(Long userId, Long wordGroupId) {
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        wordGroup.remove();
        wordGroupRepository.delete(wordGroup);
    }

    private void checkGroup(Long userId, WordGroupRequestDto requestDto) {
        if (wordGroupRepository.existsGroup(userId, requestDto.name())) {
            throw new CustomException(ErrorCode.WORD_GROUP_EXISTS);
        }
    }
}
