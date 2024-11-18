package com.numo.wordapp.service.word;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.comm.util.ProcessBuilderUtil;
import com.numo.wordapp.dto.page.PageDto;
import com.numo.wordapp.dto.word.*;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.GttsCode;
import com.numo.wordapp.entity.word.Sound;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.repository.SoundRepository;
import com.numo.wordapp.repository.word.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final SoundRepository soundRepository;

    public enum UpdateType{
        all{
            @Override
            @Transactional
            public String updateByWordType(UpdateWordDto dto, Word word){
                return "updateAll";
            }
        },
        memo{
            @Override
            public String updateByWordType(UpdateWordDto dto, Word word){
                return "updateMemo";
            }
        },
        memorization{
            @Override
            public String updateByWordType(UpdateWordDto dto, Word word) {
                return "updateMemorization";
            }
        },
        wordFolder{
            @Override
            public String updateByWordType(UpdateWordDto dto, Word word) {
                return "updateWordFolder";
            }
        };

        public abstract String updateByWordType(UpdateWordDto dto, Word word);
    }

    /*
    * 메소드: updateByWord(WordUpdateDto dto)
    * 파라미터: 요청받은 dto값
    * 기능: 데이터 수정(유의어 수정은 synonymServiceImpl에서)
    * 유의사항: word의 칼럼이 추가되면 update시에 word.set{colName}() 추가 필요
    *         !! WordUpdateDto 형식의 user_id, word_id 필수 !!
    * 작성일: 2022.06.22
    * 수정일: 2023.06.04
    * 수정내용: 2023.06.04 - 업데이트할 유의어 갯수가 더 많으면 에러났던 이슈 수정
    *         2023.06.06 - 로그인 한 사용자가 아니면 업데이트 못하도록 수정
    * */
    @Transactional
    public WordResponseDto updateByWord(Long userId, Long wordId, UpdateWordDto dto, String type){
        Word result = null;
        // 1. 해당 단어 유효한지 검색
        User user = User.builder().userId(userId).build();
        Word word = wordRepository.findByUserAndWordId(user, wordId).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));  //db에서 조회를 하면 영속성 유지..
        try{
            // 2. 업데이트 타입 확인
            UpdateType updateType = UpdateType.valueOf(type);
            String methodName = updateType.updateByWordType(dto, word);
            // 3. 타입에 알맞는 함수 실행
            Method m = this.getClass().getDeclaredMethod(methodName, UpdateWordDto.class, Word.class);
            m.setAccessible(true);  // private 메서드 접근
            result = (Word) m.invoke(this, dto, word);
        } catch (Exception e){
            throw new CustomException(ErrorCode.TYPE_NOT_FOUND);
        }

        return WordResponseDto.of(result);
    }


    /**
     * 단어 데이터 업데이트
     * updateByWord()에서 Controller로 들어온 type이 all이면 실행됨
     * @param dto {@link WordDto}
     * @param word {@link Word}
     * @return 저장된 단어 데이터 {@link Word}
     * */
    // commit 자동 수행 및 예외 발생 시 롤백
    @Transactional(rollbackFor = Exception.class)
    public WordResponseDto updateAll(UpdateWordDto dto, Word word){
        // 1. 단어 업데이트
        word.updateWord(dto);
        return WordResponseDto.of(word);
    }

    @Transactional
    public Word updateMemo(UpdateWordDto dto, Word word){
        word.updateMemo(dto.memo());
        return word;
    }

    @Transactional
    public Word updateMemorization(UpdateWordDto dto, Word word){
        word.updateMemorization(dto.memorization());
        return word;
    }

    private Word updateWordFolder(UpdateWordDto dto, Word word){
        word.setFolder(dto.folderId());
        return wordRepository.save(word);
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
     * 해당하는 단어의 음성파일이 없으면 파일 생성 및 데이터베이스에 해당하는 파일명을 저장한다.
     * @param wordName 단어명
     * */
    @Transactional
    public Long createSoundFile(String wordName, String gttsType){
        //String fileName = wordName + "_" + System.currentTimeMillis();
        String fileName = "p_"+ wordName.replaceAll("\\s", "");
        int code = 0;

        // 1. sound 테이블에 데이터 insert
        Sound sound = Sound.builder()
                .soundPath(fileName)
                .memo("")
                .build();

        sound = soundRepository.save(sound);

        // 2. 발음 파일 생성
        if (gttsType == null || gttsType.isEmpty()) {
            code = new ProcessBuilderUtil(wordName, fileName).run();
        } else {
            code = new ProcessBuilderUtil(wordName, fileName, GttsCode.valueOf(gttsType).getTTS()).run();
        }

        // 파일생성 실패 시
        if(code != 0) {
            return null;
        }

        return sound.getSoundId();
    }

    /**
     * word 데이터 삭제
    * */
    @Transactional
    public void removeWord(Long userId, Long wordId){
        User user = User.builder().userId(userId).build();
        Word word = wordRepository.findByUserAndWordId(user, wordId).orElseThrow(
               () -> new CustomException(ErrorCode.DATA_NOT_FOUND)
        );
        wordRepository.delete(word);
    }

    /**
     * 단어를 조회 한다.
     *
     * 검색, 조회 모두 해당함
     * @param userId 로그인한 유저 아이디<br>
     * @param readDto {@link ReadWordResponseDto}<br>
     * @return 단어 데이터
     */
    public ReadWordResponseDto getWord(Long userId, ReadWordRequestDto readDto){
        Slice<Word> wordsWithPage = wordRepository.findWordBy(PageRequest.of(readDto.page().getCurrent(), 20), readDto);
        List<Word> words = wordsWithPage.getContent();

        int pageNumber = wordsWithPage.getNumber();
        boolean hasNext = wordsWithPage.hasNext();

        List<WordResponseDto> dto = words.stream().map(WordResponseDto::of).toList();
        PageDto page = new PageDto(pageNumber, hasNext, getLastWordId(words));

        return new ReadWordResponseDto(dto, page);
    }

    private Long getLastWordId(List<Word> words) {
        return (long) (!words.isEmpty() ? words.size() - 1 : -1);
    }

}
