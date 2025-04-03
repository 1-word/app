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
     * @param userId 로그인한 유저
     * @param wordBookId 단어장
     * @param roleDto 수정할 권한 정보
     */
    @Transactional
    public void updateWordBookMemberRole(Long userId, Long wordBookId, WordBookRoleRequestDto roleDto) {
        WordBookMember updateMember = wordBookMemberRepository.findMemberByWordBookIdAndUserId(wordBookId, roleDto.userId());
        WordBook wordBook = updateMember.getWordBook();
        checkPermission(userId, wordBook, WordBookRole.admin);
        updateMember.setRole(roleDto.role());
    }

    /**
     * 해당 단어장에 권한 추가
     * @param userId 로그인한 유저
     * @param wordBook 단어장
     * @param roleDto 추가할 권한 정보
     */
    public void addWordBookMember(Long userId, WordBook wordBook, WordBookRoleRequestDto roleDto) {
        if (wordBookMemberRepository.existsByWordBook_IdAndUser_UserId(wordBook.getId(), roleDto.userId())) {
            throw new CustomException(ErrorCode.WORD_BOOK_MEMBER_EXISTS);
        }
        checkPermission(userId, wordBook, WordBookRole.admin);
        WordBookMember newMember = WordBookMember.builder()
                .user(new User(roleDto.userId()))
                .role(roleDto.role())
                .wordBook(wordBook).build();
        wordBookMemberRepository.save(newMember);
    }

    public List<WordBookMemberResponseDto> getWordBookMembers(Long userId, WordBook wordBook) {
        checkPermission(userId, wordBook, WordBookRole.admin);
        return wordBookMemberRepository.findMemberByWordBook(wordBook.getId());
    }

    /**
     * 단어장의 권한 체크
     * @param userId 체크할 유저(로그인한 유저)
     * @param wordBook 단어장
     */
    public void checkPermission(Long userId, WordBook wordBook, WordBookRole role) {
        // 단어장 소유자이면 추가로 멤버를 찾지 않음
        if (!wordBook.isOwner(userId)) {
            WordBookMember member = wordBookMemberRepository.findByWordBook_IdAndUser_UserId(wordBook.getId(), userId).orElseThrow(
                    () -> new CustomException(ErrorCode.UNRECOGNIZED_ROLE)
            );
            if (!checkPermission(userId, role, member)) {
                throw new CustomException(ErrorCode.UNRECOGNIZED_ROLE);
            }
        }
    }

    private boolean checkPermission(Long userId, WordBookRole role, WordBookMember member) {
        return switch (role) {
            case view -> member.hasReadPermission(userId);
            case edit -> member.hasWritePermission(userId);
            case admin -> member.hasAdminPermission(userId);
        };
    }

    public void deleteWordBookMemberRole(Long userId, WordBook wordBook, Long memberId) {
        checkPermission(userId, wordBook, WordBookRole.admin);
        wordBookMemberRepository.findMemberById(memberId);
    }
}
