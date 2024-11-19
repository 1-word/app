package com.numo.wordapp.service.word;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.comm.util.ProcessBuilderUtil;
import com.numo.wordapp.conf.PropertyConfig;
import com.numo.wordapp.dto.page.PageDto;
import com.numo.wordapp.dto.word.*;
import com.numo.wordapp.entity.word.GttsCode;
import com.numo.wordapp.entity.word.Sound;
import com.numo.wordapp.entity.word.UpdateType;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.repository.SoundRepository;
import com.numo.wordapp.repository.word.WordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final SoundRepository soundRepository;
    private final String path;

    public WordService(WordRepository wordRepository, SoundRepository soundRepository, PropertyConfig propertyConfig) {
        this.wordRepository = wordRepository;
        this.soundRepository = soundRepository;
        this.path = propertyConfig.getPath();
    }

    /**
    * 단어 저장
    * @param requestDto {@link UpdateWordDto}
    * @return Word {@link Word}
    * 작성일: 2022.06.22
    * */
    @Transactional
    public WordResponseDto saveWord(Long userId, String gttsType, WordRequestDto requestDto){
        Long fileId = null;

        if (!soundRepository.existsByWord(requestDto.word())) {
            fileId = createSoundFile(requestDto.word(), gttsType);
        }

        Word word = requestDto.toEntity(userId, gttsType, fileId);
        word.setWordDetails();

        return WordResponseDto.of(wordRepository.save(word));
    }

    /**
     * 단어 수정
     * @param userId 로그인한 유저 아이디
     * @param wordId 수정할 단어 아이디
     * @param dto 수정 데이터
     * @param type 수정 타입
     * @return 수정한 단어 데이터
     */
    @Transactional
    public WordResponseDto updateWord(Long userId, Long wordId, UpdateWordDto dto, UpdateType type) {
        UpdateWord updateWord = UpdateFactory.create(type);
        Word word = wordRepository.findByUserIdAndWordId(userId, wordId);
        Word updatedWord = updateWord.update(dto, word);
        return WordResponseDto.of(wordRepository.save(updatedWord));
    }

    /**
     * 단어를 조회 한다.
     *
     * 검색, 조회 모두 해당함
     * @param userId 로그인한 유저 아이디<br>
     * @param readDto {@link ReadWordResponseDto}<br>
     * @return 단어 데이터
     */
    public ReadWordResponseDto getWord(Long userId, PageDto pageDto, ReadWordRequestDto readDto){
        Pageable pageable = PageRequest.of(pageDto.getCurrent(), 20);
        Slice<Word> wordsWithPage = wordRepository.findWordBy(pageable, userId, pageDto.getLastWordId(),readDto);
        List<Word> words = wordsWithPage.getContent();

        int pageNumber = wordsWithPage.getNumber();
        boolean hasNext = wordsWithPage.hasNext();

        List<WordResponseDto> dto = words.stream().map(WordResponseDto::of).toList();
        pageDto = new PageDto(pageNumber, hasNext, getLastWordId(words));

        return new ReadWordResponseDto(dto, pageDto);
    }

    /**
     * 해당하는 단어의 음성파일이 없으면 파일 생성 및 데이터베이스에 해당하는 파일명을 저장한다.
     * @param wordName 단어명
     * */
    @Transactional
    public Long createSoundFile(String wordName, String gttsType){
        wordName = wordName.replaceAll("\\s", "");

        // 1. sound 테이블에 데이터 insert
        Sound sound = Sound.builder()
                .word(wordName)
                .build();

        sound = soundRepository.save(sound);

        int code = new ProcessBuilderUtil(path, wordName, GttsCode.valueOf(gttsType).getTTS()).run();

        // 파일생성 실패 시
        if (code == 0) {
           throw new CustomException(ErrorCode.SOUND_CANNOT_CREATED);
        }

        return sound.getSoundId();
    }

    /**
     * word 데이터 삭제
    * */
    public void removeWord(Long userId, Long wordId){
        Word word = wordRepository.findByUserIdAndWordId(userId, wordId);
        wordRepository.delete(word);
    }

    private Long getLastWordId(List<Word> words) {
        words = words.stream().sorted(Comparator.comparing(Word::getWordId)).toList();
        return (long) (!words.isEmpty() ? words.get(words.size() - 1).getWordId() : -1);
    }

}
