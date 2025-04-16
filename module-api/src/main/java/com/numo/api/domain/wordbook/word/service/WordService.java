package com.numo.api.domain.wordbook.word.service;

import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.dailySentence.repository.WordDailySentenceRepository;
import com.numo.api.domain.quiz.repository.QuizRepository;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import com.numo.api.domain.wordbook.detail.dto.read.ReadWordDetailListResponseDto;
import com.numo.api.domain.wordbook.detail.repository.WordDetailRepository;
import com.numo.api.domain.wordbook.service.WordBookCacheService;
import com.numo.api.domain.wordbook.sound.service.SoundService;
import com.numo.api.domain.wordbook.word.dto.*;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordResponseDto;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.api.domain.wordbook.word.service.update.UpdateFactory;
import com.numo.api.domain.wordbook.word.service.update.UpdateWord;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.page.PageDto;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.comm.page.PageResponse;
import com.numo.api.global.comm.util.JsonUtil;
import com.numo.api.listener.event.WordBookEvent;
import com.numo.batch.listener.WordBatchEvent;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBook;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.type.UpdateType;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.WordHistory;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;
import com.numo.domain.wordbook.word.dto.WordSnapShot;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WordService {
    private final WordRepository wordRepository;
    private final WordDetailRepository wordDetailRepository;
    private final WordDailySentenceRepository wordDailySentenceRepository;
    private final QuizRepository quizRepository;
    private final SoundService soundService;
    private final WordBookCacheService wordBookCacheService;
    private final WordHistoryService wordHistoryService;
    private final ApplicationEventPublisher publisher;

    /**
     * 단어 저장
     * @param userId 유저 아이디
     * @param wordBookId 단어장
     * @param gttsType 발음 타입
     * @param requestDto 저장할 단어 데이터
     * @return 저장한 단어 데이터
     */
    @Transactional
    public WordResponseDto saveWord(Long userId, Long wordBookId, GttsCode gttsType, WordRequestDto requestDto){
        // 단어장 확인
        WordBook wordBook = wordBookCacheService.findWordBook(wordBookId);

        // 발음 파일 생성
        Sound sound = soundService.createSound(requestDto.word(), gttsType);
        User user = new User(userId);

        Word word = requestDto.toEntity(user, sound, wordBook, gttsType);

        Word savedWord = wordRepository.save(word);
        String afterData = JsonUtil.toJson(WordSnapShot.copyOf(savedWord));
        AddWordHistoryDto wordHistoryData = new AddWordHistoryDto(WordHistory.Operation.INSERT, user, savedWord, null, afterData);
        wordHistoryService.saveWordHistory(wordHistoryData);
        return WordResponseDto.of(savedWord);
    }

    /**
     * 단어 수정
     * @param wordId 수정할 단어 아이디
     * @param dto 수정 데이터
     * @param type 수정 타입
     * @return 수정한 단어 데이터
     */
    public WordResponseDto updateWord(Long wordId, UpdateWordDto dto, UpdateType type) {
        UpdateWord updateWord = UpdateFactory.create(type);
        Word word = wordRepository.findByWordId(wordId);
        Word updatedWord = updateWord.update(dto, word);
        return WordResponseDto.of(wordRepository.save(updatedWord));
    }

    /**
     * 단어를 조회 한다.
     * 검색, 조회 모두 해당함
     * @param wordBookId 단어장
     * @param readDto 조회 조건
     * @return 단어 데이터
     */
    public PageResponse<ReadWordResponseDto> getWord(Long wordBookId, PageRequestDto pageDto, ReadWordRequestDto readDto){
        Pageable pageable = PageRequest.of(pageDto.current(), 30);

        Slice<WordDto> wordsWithPage = wordRepository.findWordBy(wordBookId, pageable, pageDto.lastId(), readDto);
        List<WordDto> words = wordsWithPage.getContent();

        List<Long> wordIds = words.stream().map(WordDto::wordId).toList();
        List<WordDetailResponseDto> wordDetails = wordRepository.findWordDetailByWordIds(wordIds);
        List<ReadWordDetailListResponseDto> detailGroups = WordDetailResponseDto.grouping(wordDetails);

        int pageNumber = wordsWithPage.getNumber();
        boolean hasNext = wordsWithPage.hasNext();

        List<ReadWordResponseDto> res = words.stream().map(
                word -> ReadWordResponseDto.of(word, findDetailWords(word.wordId(), detailGroups))).toList();

        PageDto pageResponse = new PageDto(pageNumber, hasNext, getLastWordId(wordIds));

        return new PageResponse<>(pageResponse, res);
    }

    public ReadWordResponseDto getWord(Long wordId) {
        WordDto wordDto = wordRepository.findWordByWordId(wordId);
        List<WordDetailResponseDto> wordDetails = wordRepository.findWordDetailByWordIds(List.of(wordId));
        List<ReadWordDetailListResponseDto> detailGroups = WordDetailResponseDto.grouping(wordDetails);
        return ReadWordResponseDto.of(wordDto, detailGroups);
    }

    /**
     * 단어 상세 데이터 리스트에서 단어와 연관된 단어 상세 데이터를 찾는다.
     * @param wordId 단어 고유번호
     * @param detailGroups 검색한 단어 상세 데이터
     * @return 해당 문장과 연관된 단어 데이터
     */
    private List<ReadWordDetailListResponseDto> findDetailWords(Long wordId, List<ReadWordDetailListResponseDto> detailGroups) {
        List<ReadWordDetailListResponseDto> result = new ArrayList<>();
        Iterator<ReadWordDetailListResponseDto> iterator = detailGroups.iterator();
        while (iterator.hasNext()) {
            ReadWordDetailListResponseDto detailGroup = iterator.next();
            if (Objects.equals(wordId, detailGroup.wordId())) {
                result.add(detailGroup);
                iterator.remove();
            }
        }
        return result;
    }

    /**
     * 해당하는 단어들을 포함하고 있는 단어 정보를 가져온다.
     * 단어 상세정보는 가져오지 않는다.
     * @param userId 유저 아이디
     * @param words 검색할 단어 리스트
     * @return 해당하는 단어들의 간단한 단어 정보(뜻, 단어, 단어장 정보 등)
     */
    public DailyWordListDto findDailyWord(Long userId, List<String> words) {
        return wordRepository.findDailyWordBy(userId, words);
    }

    /**
     * 단어장을 옮긴다.
     * 소유자만 단어의 단어장 이동 가능
     * @param targetWordBookId 옮길 단어장
     * @param wordId 옮길 단어
     */
    @Transactional
    @CacheEvict(cacheNames = "wordBook", key = "#p1")
    public void moveWordBook(Long userId, Long targetWordBookId, Long wordId) {
        Word word = wordRepository.findByWordId(wordId);
        WordBook preWordBook = word.getWordBook();
        word.updateWordBook(targetWordBookId);
        WordBook targetWordBook = wordBookCacheService.findWordBook(targetWordBookId);
        if (!targetWordBook.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }
        String memorization = word.getMemorization();
        preWordBook.decrementCount(memorization);
        publisher.publishEvent(new WordBookEvent(targetWordBookId, memorization));
    }

    /**
     * 해당 단어장의 단어를 복사한다.
     *
     * @param userId 유저
     * @param wordBookId 복사할 단어장
     * @param targetWordBookId 복사 대상 단어장
     */
    public void copyWord(Long userId, Long wordBookId, Long targetWordBookId) {
        WordBook targetWordBook = wordBookCacheService.findWordBook(targetWordBookId);
        if (!targetWordBook.isOwner(userId)) {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }
        publisher.publishEvent(new WordBatchEvent(userId, wordBookId, targetWordBookId));
    }

    /**
     * 단어 데이터 단건 삭제
     * @param wordId 단어
     */
    @Transactional
    public void removeWord(Long wordId){
        Word word = wordRepository.findByWordId(wordId);
        removeRelatedData(List.of(word));
        wordRepository.delete(word);
    }

    /**
     * 단어장의 단어 데이터 삭제
     * @param wordBookId 단어장
     */
    @Transactional
    public void removeWordsByWordBook(Long wordBookId) {
        List<Word> words = wordRepository.findByWordBook_id(wordBookId);
        removeRelatedData(words);
        wordRepository.deleteByWordBook_id(wordBookId);
    }

    public WordCountDto getWordCountByWordBookId(Long wordBookId) {
        return wordRepository.findCountByWordBookId(wordBookId);
    }

    /**
     * 단어를 삭제한다
     * 연관 관계에 있는 모든 데이터를 삭제한다.
     * @param words 삭제할 단어 리스트
     */
    private void removeRelatedData(List<Word> words){
        List<Long> wordIds = words.stream().map(Word::getWordId).toList();
        quizRepository.deleteByWord_WordIdIn(wordIds);
        wordDailySentenceRepository.deleteByWord_WordIdIn(wordIds);
        wordDetailRepository.deleteByWord_WordIdIn(wordIds);
        // todo 삭제 이후 오늘의 내 문장 태그 업데이트 필요
    }

    private Long getLastWordId(List<Long> wordIds) {
        return !wordIds.isEmpty()? wordIds.get(wordIds.size() - 1) : null;
    }

}
