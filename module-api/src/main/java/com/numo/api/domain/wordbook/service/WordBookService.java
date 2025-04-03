package com.numo.api.domain.wordbook.service;

import com.numo.api.domain.wordbook.dto.WordBookMemberResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookRequestDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
import com.numo.api.domain.wordbook.dto.WordBookRoleRequestDto;
import com.numo.api.domain.wordbook.repository.WordBookRepository;
import com.numo.api.domain.wordbook.repository.query.WordBookQueryRepository;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.WordBookRole;
import com.numo.domain.wordbook.dto.WordBookUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordBookService {

    private final WordBookMemberService wordBookMemberService;
    private final WordBookRepository wordBookRepository;
    private final WordBookQueryRepository wordBookQueryRepository;

    /**
     * 내 단어장 다건 조회
     * @param userId 유저 아이디
     * @return 조회한 단어장 데이터
     */
    public List<WordBookResponseDto> getWordBooks(Long userId){
        return wordBookQueryRepository.findWordBooksByUserId(userId);
    }

    /**
     * 공유 단어장 다건 조회
     * @param userId 유저 아이디
     * @return 조회한 공유 단어장 데이터
     */
    public List<WordBookResponseDto> getShareWordBooks(Long userId) {
        return wordBookQueryRepository.findShareWordBooks(userId);
    }

    /**
     * 단어장 단건 조회
     * 읽기 권한이 있는 멤버, 소유자만 조회 가능
     * @param userId 유저 아이디
     * @param wordBookId 단어장 아이디
     * @return 조회한 단어장 단건 데이터
     */
    public WordBookResponseDto getWordBook(Long userId, Long wordBookId) {
        WordBook wordBook = findWordBook(wordBookId);
        wordBookMemberService.checkPermission(userId, wordBook, WordBookRole.view);
        return WordBookResponseDto.of(wordBook);
    }

    /**
     * 단어장 생성
     * @param userId 유저 아이디
     * @param wordBookDto 저장할 단어장 데이터
     * @return 저장한 단어장 데이터
     */
    public WordBookResponseDto saveWordBook(Long userId, WordBookRequestDto wordBookDto) {
        WordBook wordBook = wordBookDto.toEntity(userId);
        return WordBookResponseDto.of(wordBookRepository.save(wordBook));
    }

    /**
     * 단어장 수정
     * @param userId 유저 아이디
     * @param wordBookId 단어장 아이디
     * @param WordBookDto 단어장 데이터
     * @return 수정된 단어장 데이터
     */
    @Transactional
    public WordBookResponseDto updateWordBook(Long userId, Long wordBookId, WordBookUpdateDto WordBookDto){
        WordBook wordBook = findWordBook(wordBookId);
        wordBookMemberService.checkPermission(userId, wordBook, WordBookRole.admin);
        wordBook.update(WordBookDto);
        return WordBookResponseDto.of(wordBook);
    }

    /**
     * 단어장 삭제
     * 본인 단어장이 아니거나 단어장에 단어가 있으면 삭제 실패
     * @param userId 유저 아이디
     * @param wordBookId 폴더 아이디
     */
    public void removeWordBook(Long userId, Long wordBookId) {
        WordBook wordBook = findWordBook(wordBookId);
        if (!wordBook.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

        if(!wordBook.isDeleteAllowed()) {
            throw new CustomException(ErrorCode.ASSOCIATED_DATA_EXISTS);
        }

        wordBook.removeMember();
        wordBookRepository.delete(wordBook);
    }

    /**
     * 단어를 옮기기 위해 이전 단어장의 단어 수를 업데이트 해준다.
     * @param preWordBookId
     * @param memorization
     */
    @Transactional
    public void decrementPreviousWordBookCount(Long preWordBookId, String memorization) {
        WordBook wordBook = findWordBook(preWordBookId);
        wordBook.deleteCount(memorization);
    }

    public WordBook findWordBook(Long id) {
        return wordBookRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
    }

    public void addWordBookMember(Long userId, Long wordBookId, WordBookRoleRequestDto roleDto) {
        WordBook wordBook = findWordBook(wordBookId);
        wordBookMemberService.addWordBookMember(userId, wordBook, roleDto);
    }

    public void updateWordBookMemberRole(Long userId, Long wordBookId, WordBookRoleRequestDto roleDto) {
        wordBookMemberService.updateWordBookMemberRole(userId, wordBookId, roleDto);
    }

    public List<WordBookMemberResponseDto> getWordBookMembers(Long userId, Long wordBookId) {
        WordBook wordBook = findWordBook(wordBookId);
        return wordBookMemberService.getWordBookMembers(userId, wordBook);
    }

    public void deleteWordBookMemberRole(Long userId, Long wordBookId, Long memberId) {
        WordBook wordBook = findWordBook(wordBookId);
        wordBookMemberService.deleteWordBookMemberRole(userId, wordBook, memberId);
    }
}
