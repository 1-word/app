package com.numo.api.domain.wordbook.word.service;

import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import com.numo.api.domain.wordbook.detail.dto.read.ReadWordDetailListResponseDto;
import com.numo.api.domain.wordbook.folder.service.FolderService;
import com.numo.api.domain.wordbook.sound.repository.SoundRepository;
import com.numo.api.domain.wordbook.word.dto.WordDto;
import com.numo.api.domain.wordbook.word.dto.WordRequestDto;
import com.numo.api.domain.wordbook.word.dto.WordResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordListResponseDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordResponseDto;
import com.numo.api.domain.wordbook.word.repository.WordRepository;
import com.numo.api.domain.wordbook.word.service.update.UpdateFactory;
import com.numo.api.domain.wordbook.word.service.update.UpdateWord;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.gtts.Gtts;
import com.numo.api.global.comm.gtts.GttsService;
import com.numo.api.global.comm.page.PageDto;
import com.numo.api.global.comm.page.PageRequestDto;
import com.numo.api.global.conf.PropertyConfig;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.word.dto.UpdateWordDto;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.type.UpdateType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class WordService {

    private final WordRepository wordRepository;
    private final SoundRepository soundRepository;
    private final FolderService folderService;
    private final String path;
    private final GttsService gttsService;

    public WordService(WordRepository wordRepository,
                       SoundRepository soundRepository,
                       FolderService folderService,
                       PropertyConfig propertyConfig,
                       GttsService gttsService) {
        this.wordRepository = wordRepository;
        this.soundRepository = soundRepository;
        this.folderService = folderService;
        this.path = propertyConfig.getGttsPath();
        this.gttsService = gttsService;
    }

    /**
     * 단어 저장
      * @param userId 유저 아이디
     * @param gttsType 발음 타입
     * @param requestDto 저장할 단어 데이터
     * @return 저장한 단어 데이터
     */
    @Transactional
    public WordResponseDto saveWord(Long userId, String gttsType, WordRequestDto requestDto){
        Long soundId = null;
        String wordName = requestDto.word().replaceAll("\\s", "");

        // 폴더 확인
        if (requestDto.folderId() != null && !folderService.existsFolder(requestDto.folderId(), userId)) {
            throw new CustomException(ErrorCode.FOLDER_NOT_FOUND);
        }

        // 단어 그룹 확인

        // 발음 파일 생성
        Sound sound = soundRepository.findByWord(requestDto.word());

        if (sound == null) {
            createSoundFile(wordName, gttsType);
            // sound 테이블에 데이터 insert
            sound = Sound.builder()
                    .word(wordName)
                    .build();
            sound = soundRepository.save(sound);
        }
        soundId = sound.getSoundId();

        Word word = requestDto.toEntity(userId, gttsType, soundId);
//        word.setWordDetails();

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

    @Transactional
    public void moveFolder(Long userId, Long wordId, Long folderId) {
        Word word = wordRepository.findByUserIdAndWordId(userId, wordId);
        if (!folderService.existsFolder(folderId, userId)) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        word.setWordbook(folderId);
    }

    /**
     * 단어를 조회 한다.
     *
     * 검색, 조회 모두 해당함
     * @param userId 로그인한 유저 아이디<br>
     * @param readDto {@link ReadWordListResponseDto}<br>
     * @return 단어 데이터
     */
    public ReadWordListResponseDto getWord(Long userId, PageRequestDto pageDto, ReadWordRequestDto readDto){
        Pageable pageable = PageRequest.of(pageDto.current(), 30);

        Slice<WordDto> wordsWithPage = wordRepository.findWordBy(pageable, userId, pageDto.lastId(), readDto);
        List<WordDto> words = wordsWithPage.getContent();

        List<Long> wordIds = words.stream().map(WordDto::wordId).toList();
        List<WordDetailResponseDto> wordDetails = wordRepository.findWordDetailByIds(wordIds);
        List<ReadWordDetailListResponseDto> detailGroups = WordDetailResponseDto.grouping(wordDetails);

        int pageNumber = wordsWithPage.getNumber();
        boolean hasNext = wordsWithPage.hasNext();

        List<ReadWordResponseDto> dto = words.stream().map(
                word -> ReadWordResponseDto.of(word, findDetailWords(word.wordId(), detailGroups))).toList();

        PageDto pageResponse = new PageDto(pageNumber, hasNext, getLastWordId(wordIds));

        return new ReadWordListResponseDto(dto, pageResponse);
    }

    public ReadWordResponseDto getWord(Long userId, Long wordId) {
        WordDto wordDto = wordRepository.findWordByWordId(userId, wordId);
        List<WordDetailResponseDto> wordDetails = wordRepository.findWordDetailByIds(List.of(wordId));
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
     * 해당하는 단어의 음성파일이 없으면 파일 생성 및 데이터베이스에 해당하는 파일명을 저장한다.
     * @param wordName 단어명
     * */
    @Transactional(propagation = Propagation.NEVER)
    public void createSoundFile(String wordName, String gttsType){
        String lang = GttsCode.valueOf(gttsType).name();
        String savePath = path + "/" + wordName + ".mp3";
        Gtts gtts = new Gtts(wordName, lang, savePath);

        gttsService.saveAudio(gtts);
    }

    /**
     * word 데이터 삭제
    * */
    public void removeWord(Long userId, Long wordId){
        Word word = wordRepository.findByUserIdAndWordId(userId, wordId);
        wordRepository.delete(word);
    }

    private Long getLastWordId(List<Long> wordIds) {
        return !wordIds.isEmpty()? wordIds.get(wordIds.size() - 1) : null;
    }
}
