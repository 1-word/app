package com.numo.wordapp.service.impl;

import com.numo.wordapp.comm.advice.exception.CustomException;
import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.model.Folder;
import com.numo.wordapp.model.Sound;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.repository.FolderRepository;
import com.numo.wordapp.repository.SoundRepository;
import com.numo.wordapp.repository.SynonymRepository;
import com.numo.wordapp.repository.WordRepository;

import com.numo.wordapp.security.model.WordType;
import com.numo.wordapp.service.WordService;
import com.numo.wordapp.util.ProcessBuilderUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 클래스: WordServiceImpl
 내용: Word, Synonym 데이터 CRUD 관리
 작성자: 정현경
 작성일: 2022.05.25
 수정이력: 2022.05.25 WordServiceImpl 작성_정현경
          2022.06.22 CURD 작성_정현경
          2022.09.11 update 로직 수정/SynonymService삭제 후 해당 기능 update로 이전_정현경
*/

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final SynonymRepository synonymRepository;
    private final SoundRepository soundRepository;
    private final FolderRepository folderRepository;

    public enum UpdateType{
        all{
            @Override
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word){
                return updateAll(jpaRepositories, dto, word);
            }
        },
        memo{
            @Override
            public String updateByWordType(List<JpaRepository> jpaRepositories,WordDto.Request dto, Word word){
                 WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateMemo(wordRepository, dto, word);
            }
        },
        memorization{
            @Override
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word) {
                WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateMemorization(wordRepository, dto, word);
            }
        },
        wordFolder{
            @Override
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word) {
                WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateWordFolder(wordRepository, dto, word);
            }
        };

        public abstract String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word);
    }

    public WordServiceImpl(WordRepository wordRepository,
                           SynonymRepository synonymRepository,
                           SoundRepository soundRepository,
                           FolderRepository folderRepository){
        this.wordRepository = wordRepository;
        this.synonymRepository = synonymRepository;
        this.soundRepository = soundRepository;
        this.folderRepository = folderRepository;
    }

    /*
    * 메소드: updateByWord(WordDto.Request dto)
    * 파라미터: 요청받은 dto값
    * 기능: 데이터 수정(유의어 수정은 synonymServiceImpl에서)
    * 유의사항: word의 칼럼이 추가되면 update시에 word.set{colName}() 추가 필요
    *         !! WordDto.Request 형식의 user_id, word_id 필수 !!
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * 수정일: 2023.06.04
    * 수정내용: 2023.06.04 - 업데이트할 유의어 갯수가 더 많으면 에러났던 이슈 수정
    *         2023.06.06 - 로그인 한 사용자가 아니면 업데이트 못하도록 수정
    * */
    @Override
    @Transactional
    public String updateByWord(WordDto.Request dto, String type){
        // 1. 해당 단어 유효한지 검색
        Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound.getDescription()));  //db에서 조회를 하면 영속성 유지..
        try{
            UpdateType updateType = UpdateType.valueOf(type);
            List<JpaRepository> jpaRepositories = new ArrayList<>();
            jpaRepositories.add(wordRepository);
            jpaRepositories.add(synonymRepository);
            updateType.updateByWordType(jpaRepositories, dto, word);
        }catch (Exception e){
            System.out.println(e);
            throw new CustomException(ErrorCode.TypeNotFound.getDescription());
        }

        return "저장완료";
    }

    public static String updateAll(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word){
        WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
        SynonymRepository synonymRepository = (SynonymRepository) jpaRepositories.get(1);
        // 2 단어 업데이트
        //단어 테이블 컬럼 추가 시 아래 코드 작성 필요
        //word.setWord(dto.getWord());  //word는 업데이트 불필요(pk값)_20230701
        word.setMean(dto.getMean());
        word.setWread(dto.getWread());
        if (dto.getMemo() != null) word.setMemo(dto.getMemo());
        word.setUpdate_time(LocalDateTime.now());
        wordRepository.save(word);

        // 3. 유의어 업데이트
        List<Synonym> existingSynonyms = word.getSynonyms();
        List<SynonymDto.Request> inputSynonyms = dto.getSynonyms();

        // 현재 유의어 갯수 확인
        for (int i=0; i < existingSynonyms.size(); i++){
            Synonym existingSynonym = existingSynonyms.get(i);
            if (i < inputSynonyms.size()){
                SynonymDto.Request inputSynonym = inputSynonyms.get(i);
                existingSynonym.setSynonym(inputSynonym.getSynonym());
                existingSynonym.setMemo(inputSynonym.getMemo());
                synonymRepository.save(existingSynonym);
            }else{  // 현재 유의어 갯수보다 적으면 delete 실행
                synonymRepository.deleteSynonym(existingSynonym.getSynonym_id());
            }
        }

        // 현재 유의어 갯수보다 update할 유의어 갯수가 더 많으면 insert 실행
        for (int i = existingSynonyms.size(); i < inputSynonyms.size(); i++) {
            SynonymDto.Request inputSynonym = inputSynonyms.get(i);
            Synonym newSynonym = Synonym.builder()
                    .synonym(inputSynonym.getSynonym())
                    .memo(inputSynonym.getMemo())
                    .word(word)
                    .build();
            synonymRepository.save(newSynonym);
        }
        return "완료";
    }

    private static String updateMemo(WordRepository wordRepository, WordDto.Request dto, Word word){
        word.setMemo(dto.getMemo());
        wordRepository.save(word);
        return "완료";
    }

    private static String updateMemorization(WordRepository wordRepository, WordDto.Request dto, Word word){
        word.setMemorization(dto.getMemorization());
        wordRepository.save(word);
        return "완료";
    }

    private static String updateWordFolder(WordRepository wordRepository, WordDto.Request dto, Word word){
        Folder folder = new Folder();
        folder.setFolderId(dto.getFolder_id());
        word.setFolder(folder);
        wordRepository.save(word);
        return "완료";
    }

    /**
    * 메소드: setByWord(WordDto.Request dto)
    * @param dto {@link WordDto.Request}
    * @return Word {@link Word}
    * @note
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */

    @Override
    @Transactional
    public Word setByWord(WordDto.Request dto, String type){
        Word word = dto.toEntity();
        List<SynonymDto.Request> synonyms = dto.getSynonyms();
        for (SynonymDto.Request synonym : synonyms) {
            word.addSynonym(synonym.toEntity(word));
        }

        //folder_id 입력
        if (dto.getFolder_id() != null) {
            Folder folder = new Folder();
            folder.setFolderId(dto.getFolder_id());
            word.setFolder(folder);
        }

        word.setType(WordType.valueOf(type));

        Optional<String> soundPathOptional = soundRepository.findByWord(word.getWord())
                .map(sound -> sound.getSoundPath());

        String fileName = soundPathOptional.orElse(null);
        if(fileName == null || fileName == ""){ // 해당 하는 단어의 sound가 없으면 sound파일 생성 및 데이터베이스에 추가
            fileName = createSoundFile(word.getWord(), type);
        }
        word.setSoundPath(fileName);
        return wordRepository.save(word);
    }
    /**
     * 해당하는 단어의 음성파일이 없으면 파일 생성 및 데이터베이스에 해당하는 파일명을 저장한다.
     * @param wordName 단어명
     * */
    private String createSoundFile(String wordName, String type){
        //String fileName = wordName + "_" + System.currentTimeMillis();
        String fileName = "p_"+ wordName.replaceAll("\\s", "");
        int code = 0;

        // 1. sound 테이블에 데이터 insert
        Sound sound = Sound.builder()
                .word(wordName)
                .soundPath(fileName)
                .memo("")
                .build();

        soundRepository.save(sound);

        // 2. 발음 파일 생성
        if(type == null || type == "") {
            code = new ProcessBuilderUtil(wordName, fileName).run();
        }else{
           code = new ProcessBuilderUtil(wordName, fileName, WordType.valueOf(type).getTtsType()).run();
        }

        //파일생성 실패 시
        if(code != 0) fileName = "";
        return fileName;
    }

    /*
    * 메소드: removeByWord(int id)
    * 파라미터: word 기본 키 값
    * 리턴 값: String
    * 기능: 해당하는 키 값 데이터 삭제
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */

    @Override
    public String removeByWord(WordDto.Request dto){
       Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound.getDescription()));
       wordRepository.delete(word);
//        word.ifPresent(removeWord->{
//            wordRepository.delete(removeWord);
//        });

        return "데이터 삭제를 완료하였습니다.";
    }

    /*
    메소드: getBySearchWord(int word_id)
    파라미터: word 기본 키 값
    리턴 값: Word
    기능: 검색한 단어(Word) 및 유의어(Synonym) 조회
    주의사항: 양방향 참조이기때문에 DTO를 이용해서 리턴해야함 (무한 참조 발생 가능)
    작성자: 정현경
    작성일: 2022.05.25
    */

    /*@Override
    public Word getBySearchWord(int word_id){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, wread, memo);
        //return wordRepository.findById(word_id).get();
    }*/

    @Override
    public List<Word> getBySearchWord(String user_id, String data){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, wread, memo);
        //return wordRepository.findById(word_id).get();
        return wordRepository.getBySearchWord(user_id, data);
    }

    /*
    메소드: getByAllWord()
    리턴 값: List<Word>
    기능: 모든 단어(Word) 및 유의어(Synonym) 조회
    주의사항: 양방향 참조이기때문에 DTO를 이용해서 리턴해야함 (무한 참조 발생 가능)
    작성자: 정현경
    작성일: 2022.05.25
    */

    @Override
    public List<Word> getByAllWord(String user_id){
        //return wordRepository.getByAllWord(Sort.by(Sort.Direction.DESC, "word.word_id", "synonym_id"));
        return wordRepository.getByAllWord(user_id);
    }

    @Override
    public List<Word> getByFolderWord(String user_id, int folder_id){
        return wordRepository.getByFolderWord(user_id, folder_id);
    }
}
