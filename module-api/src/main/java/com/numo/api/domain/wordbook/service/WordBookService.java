package com.numo.api.domain.wordbook.service;

import com.numo.api.domain.wordbook.dto.WordBookRequestDto;
import com.numo.api.domain.wordbook.dto.WordBookResponseDto;
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
     * @param wordBookId 단어장 아이디
     * @return 조회한 단어장 단건 데이터
     */
    public WordBookResponseDto getWordBook(Long wordBookId) {
        WordBook wordBook = findWordBook(wordBookId);
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
     * @param wordBookId 단어장 아이디
     * @param WordBookDto 단어장 데이터
     * @return 수정된 단어장 데이터
     */
    @Transactional
    public WordBookResponseDto updateWordBook(Long wordBookId, WordBookUpdateDto WordBookDto){
        WordBook wordBook = findWordBook(wordBookId);
        wordBook.update(WordBookDto);
        return WordBookResponseDto.of(wordBook);
    }

    /**
     * 단어장 삭제
     * 본인 단어장이 아니거나 단어장에 단어가 있으면 삭제 실패
     *
     * @param wordBookId  폴더 아이디
     * @param removeWords 단어 삭제 여부
     */
    public void removeWordBook(Long wordBookId, boolean removeWords) {
        WordBook wordBook = findWordBook(wordBookId);

        if (!removeWords && !wordBook.isDeleteAllowed()) {
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

    /**
     * 단어장 권한 체크
     * @param userId 유저
     * @param wordBookId 단어장
     * @param targetRole 체크할 권한
     * @return 권한 여부
     */
    public boolean hasPermission(Long userId, Long wordBookId, WordBookRole targetRole) {
        WordBook wordBook = findWordBook(wordBookId);
        return wordBook.hasPermission(userId, targetRole);
    }

}
