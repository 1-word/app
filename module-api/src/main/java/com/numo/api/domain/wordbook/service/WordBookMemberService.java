package com.numo.api.domain.wordbook.service;

import com.numo.api.domain.wordbook.dto.WordBookMemberResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookRoleRequestDto;
import com.numo.api.domain.wordbook.repository.WordBookMemberRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.WordBookMember;
import com.numo.domain.wordbook.WordBookRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordBookMemberService {
    private final WordBookMemberRepository wordBookMemberRepository;

    /**
     * 해당 단어장에 권한이 있는 사용자만 권한 수정
     * @param wordBookId 단어장
     * @param roleDto 수정할 권한 정보
     */
    @Transactional
    public void updateWordBookMemberRole(Long wordBookId, WordBookRoleRequestDto roleDto) {
        WordBookMember updateMember = wordBookMemberRepository.findMemberByWordBookIdAndUserId(wordBookId, roleDto.userId());
        updateMember.setRole(roleDto.role());
    }

    /**
     * 해당 단어장에 권한 추가
     * @param wordBookId 단어장
     * @param roleDto 추가할 권한 정보
     */
    public void addWordBookMember(Long wordBookId, WordBookRoleRequestDto roleDto) {
        if (wordBookMemberRepository.existsByWordBook_IdAndUser_UserId(wordBookId, roleDto.userId())) {
            throw new CustomException(ErrorCode.WORD_BOOK_MEMBER_EXISTS);
        }
        WordBookMember newMember = WordBookMember.builder()
                .user(new User(roleDto.userId()))
                .wordBook(new WordBook(wordBookId))
                .role(roleDto.role())
                .build();
        wordBookMemberRepository.save(newMember);
    }

    /**
     * 단어장의 멤버 조회
     * @param wordBookId 단어장
     * @return 단어장 멤버 리스트
     */
    public List<WordBookMemberResponseDto> getWordBookMembers(Long wordBookId) {
        return wordBookMemberRepository.findMemberByWordBook(wordBookId);
    }

    /**
     * 단어장의 멤버 삭제
     * @param wordBookMemberId 멤버
     */
    public void deleteWordBookMemberRole(Long wordBookMemberId) {
        WordBookMember member = wordBookMemberRepository.findMemberById( wordBookMemberId);
        wordBookMemberRepository.delete(member);
    }

    /**
     * 단어장 멤버 권한 확인
     * @param userId 확인할 유저(로그인한 유저)
     */
    public void checkPermission(Long userId, Long wordBookId, WordBookRole targetRole) {
        WordBookMember member = wordBookMemberRepository.findByWordBook_IdAndUser_UserId(wordBookId, userId).orElseThrow(
                () -> new CustomException(ErrorCode.UNRECOGNIZED_ROLE)
        );
        if (!checkPermission(member, targetRole)) {
            throw new CustomException(ErrorCode.UNRECOGNIZED_ROLE);
        }
    }

    private boolean checkPermission(WordBookMember member, WordBookRole targetRole) {
        return switch (targetRole) {
            case view -> member.hasReadPermission();
            case edit -> member.hasWritePermission();
            case admin -> member.hasAdminPermission();
        };
    }
}
