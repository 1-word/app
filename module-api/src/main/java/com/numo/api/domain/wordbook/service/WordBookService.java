package com.numo.api.domain.wordbook.service;

import com.numo.api.domain.wordbook.dto.*;
import com.numo.api.domain.wordbook.repository.WordBookMemberRepository;
import com.numo.api.domain.wordbook.repository.WordBookRepository;
import com.numo.api.domain.wordbook.repository.query.WordBookQueryRepository;
import com.numo.api.domain.wordbook.word.dto.WordCountDto;
import com.numo.api.domain.wordbook.word.service.WordService;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.WordBookRole;
import com.numo.domain.wordbook.dto.WordBookUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WordBookService {
    private final WordBookCacheService wordBookCacheService;
    private final WordBookRepository wordBookRepository;
    private final WordBookQueryRepository wordBookQueryRepository;
    private final WordBookMemberRepository wordBookMemberRepository;
    private final WordService wordService;

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
    public List<ShareWordBookResponseDto> getShareWordBooks(Long userId) {
        return wordBookQueryRepository.findShareWordBooks(userId);
    }

    /**
     * 단어장 단건 조회
     * @param wordBookId 단어장 아이디
     * @return 조회한 단어장 단건 데이터
     */
    public WordBookResponseDto getWordBook(Long wordBookId) {
        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);
//        WordBook wordBook = wordBookRepository.findWordBookById(wordBookId);
        return WordBookResponseDto.of(wordBook);
    }

    public WordBookResponseDto getWordBook_NoCache(Long wordBookId) {
//        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);
        WordBook wordBook = wordBookRepository.findWordBookById(wordBookId);
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
     * @param wordBookDto 단어장 데이터
     * @return 수정된 단어장 데이터
     */
    @CacheEvict(cacheNames = "wordBook", key = "#p0")
    public WordBookResponseDto updateWordBook(Long wordBookId, WordBookUpdateDto wordBookDto){
        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);
        wordBook.update(wordBookDto);
        WordBook savedWordBook = wordBookRepository.save(wordBook);
        return WordBookResponseDto.of(savedWordBook);
    }

    /**
     * 단어장 삭제, 쉐어룸에 공유된 상태라면 삭제 불가
     * 단어 삭제 여부가 true이면 단어 데이터 모두 삭제
     *
     * @param wordBookId  폴더 아이디
     * @param removeWords 단어 삭제 여부
     */
    @CacheEvict(cacheNames = "wordBook", key = "#p0")
    @Transactional
    public void removeWordBook(Long wordBookId, boolean removeWords) {
        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);

        if (wordBook.isShared()) {
            throw new CustomException(ErrorCode.SHARE_ROOM_WORD_BOOK_IS_NOT_DELETE);
        }

        if (!removeWords && !wordBook.isDeleteAllowed()) {
            throw new CustomException(ErrorCode.ASSOCIATED_DATA_EXISTS);
        }

        if (removeWords) {
            wordService.removeAllWordsByWordBook(wordBookId);
        }

        wordBookMemberRepository.deleteByWordBook_Id(wordBookId);
        wordBookRepository.delete(wordBook);
    }

    /**
     * 단어를 옮기기 위해 이전 단어장, 옮길 단어장의 단어 수를 업데이트 해준다.
     * @param wordBookId
     * @param memorization
     */
    @Transactional
    @CacheEvict(cacheNames = "wordBook", key = "#p0")
    public void incrementWordCount(Long wordBookId, String memorization) {
        WordBook wordBook = wordBookRepository.findWordBookById(wordBookId);
        wordBook.incrementCount(memorization);
    }

    /**
     * 단어장 권한 체크
     * @param userId 유저
     * @param wordBookId 단어장
     * @param targetRole 체크할 권한
     * @return 권한 여부
     */
    public boolean hasPermission(Long userId, Long wordBookId, WordBookRole targetRole) {
        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);
        return wordBook.hasPermission(userId, targetRole);
    }

    @Transactional
    public void asyncWordCount(Long wordBookId, WordCountDto wordCount) {
        WordBook wordBook = wordBookRepository.findWordBookById(wordBookId);
        wordBook.updateCount(
                wordCount.totalCount().intValue(),
                wordCount.memorizedCount().intValue(),
                wordCount.unMemorizedCount().intValue()
        );
    }

    public WordBookSettingDto getWordBookSetting(Long wordBookId) {
        return wordBookQueryRepository.findSettingById(wordBookId);
    }

    @Transactional
    @CacheEvict(cacheNames = "wordBook", key = "#p0")
    public void updateWordBookSetting(Long wordBookId, WordBookSettingDto settingDto) {
        WordBook wordBook = wordBookRepository.findWordBookById(wordBookId);
        wordBook.settingUpdate(settingDto.isShared(), settingDto.anyoneBasicRole(), settingDto.memberBasicRole());
    }

    public List<WordBookCountResponse> getWordBooksCount(Long userId) {
        long start = System.nanoTime();
        List<WordBookCountResponse> wordBooksCount = wordBookRepository.findWordBooksCount(userId);
        long end = System.nanoTime();
        System.out.println("time: " + (end - start) / 1000_0000);
        return wordBooksCount;
    }

    public List<WordBookResponseDto> getWordBooksGrouping(Long userId) {
        long start = System.nanoTime();
        List<InWordCountDto> wordsCount = wordBookRepository.findWordsCount(userId);
        long end = System.nanoTime();
        System.out.println("time: " + (end - start) / 1000_0000);
        return wordBookQueryRepository.getWordBooksGrouping(userId, wordsCount);
    }
}
