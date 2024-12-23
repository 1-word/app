package com.numo.wordapp.service.word;

import com.numo.domain.word.detail.WordGroup;
import com.numo.domain.word.detail.dto.WordGroupRequestDto;
import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.word.group.ReadWordGroupResponseDto;
import com.numo.wordapp.dto.word.group.WordGroupResponseDto;
import com.numo.wordapp.repository.word.WordDetailRepository;
import com.numo.wordapp.repository.word.group.WordGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordGroupService {
    private final WordGroupRepository wordGroupRepository;
    private final WordDetailRepository wordDetailRepository;

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
     * 해당하는 유저, 품사 아이디의 품사 데이터를 가져온다.
     * @param userId 유저 아이디
     * @param wordGroupId 품사 아이디
     * @return 조회한 품사 데이터
     */
    @Transactional
    public ReadWordGroupResponseDto getWordGroup(Long userId, Long wordGroupId) {
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        return ReadWordGroupResponseDto.of(wordGroup);
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
        checkDefaultGroup(wordGroupId);
        // 동일한 품사명은 저장 불가
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        checkGroup(userId, requestDto);
        wordGroup.update(requestDto);
        return WordGroupResponseDto.of(wordGroup);
    }

    /**
     * 품사 삭제, 연관된 품사 상세 데이터가 있으면 삭제하지 않는다.
     * 기본 품사는 삭제가 불가능
     * @param userId 유저 아이디
     * @param wordGroupId 품사 아이디
     */
    public void removeWordGroup(Long userId, Long wordGroupId) {
        checkDefaultGroup(wordGroupId);
        WordGroup wordGroup = wordGroupRepository.findWordGroupByIdAndUser(wordGroupId, userId);
        if (wordDetailRepository.existsByWordGroupAndWord_User_UserId(wordGroup, userId)) {
            throw new CustomException(ErrorCode.WORD_GROUP_DELETE_FAILED);
        }

        wordGroupRepository.delete(wordGroup);
    }

    private void checkDefaultGroup(Long wordGroupId) {
        if (wordGroupRepository.existsByWordGroupIdAndDefaultGroup(wordGroupId, "Y")) {
            throw new CustomException(ErrorCode.DEFAULT_GROUP_EDIT_FAILED);
        }
    }

    private void checkGroup(Long userId, WordGroupRequestDto requestDto) {
        if (wordGroupRepository.existsGroup(userId, requestDto.name())) {
            throw new CustomException(ErrorCode.WORD_GROUP_EXISTS);
        }
    }
}
