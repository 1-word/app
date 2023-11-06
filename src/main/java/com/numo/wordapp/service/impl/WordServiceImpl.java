package com.numo.wordapp.service.impl;

import com.numo.wordapp.comm.advice.exception.CustomException;
import com.numo.wordapp.dto.WordDetailMainDto;
import com.numo.wordapp.dto.WordDetailSubDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.model.word.Folder;
import com.numo.wordapp.model.word.Sound;
import com.numo.wordapp.model.word.Word;
import com.numo.wordapp.model.word.GttsCode;
import com.numo.wordapp.model.word.detail.WordDetailMain;
import com.numo.wordapp.model.word.detail.WordDetailSub;
import com.numo.wordapp.model.word.detail.WordDetailTitle;
import com.numo.wordapp.repository.*;

import com.numo.wordapp.service.WordService;
import com.numo.wordapp.util.ProcessBuilderUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final WordDetailMainRepository wordDetailMainRepository;
    private final WordDetailSubRepository wordDetailSubRepository;

    public WordServiceImpl(WordRepository wordRepository,
                           SynonymRepository synonymRepository,
                           SoundRepository soundRepository,
                           FolderRepository folderRepository,
                           WordDetailMainRepository wordDetailMainRepository,
                           WordDetailSubRepository wordDetailSubRepository){
        this.wordRepository = wordRepository;
        this.synonymRepository = synonymRepository;
        this.soundRepository = soundRepository;
        this.folderRepository = folderRepository;
        this.wordDetailMainRepository = wordDetailMainRepository;
        this.wordDetailSubRepository = wordDetailSubRepository;
    }

    public enum UpdateType{
        all{
            @Override
            @Transactional
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word){
                return updateAll(jpaRepositories, dto, word);
            }
        },
        memo{
            @Override
            @Transactional
            public String updateByWordType(List<JpaRepository> jpaRepositories,WordDto.Request dto, Word word){
                 WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateMemo(wordRepository, dto, word);
            }
        },
        memorization{
            @Override
            @Transactional
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word) {
                WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateMemorization(wordRepository, dto, word);
            }
        },
        wordFolder{
            @Override
            @Transactional
            public String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word) {
                WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
                return updateWordFolder(wordRepository, dto, word);
            }
        };

        public abstract String updateByWordType(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word);
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
        Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));  //db에서 조회를 하면 영속성 유지..
        try{
            UpdateType updateType = UpdateType.valueOf(type);
            List<JpaRepository> jpaRepositories = new ArrayList<>();
            jpaRepositories.add(wordRepository);
            jpaRepositories.add(synonymRepository);
            updateType.updateByWordType(jpaRepositories, dto, word);
        }catch (Exception e){
            System.out.println(e);
            throw new CustomException(ErrorCode.TypeNotFound);
        }

        return "저장완료";
    }


    /**
     * 단어 데이터 업데이트
     * @param jpaRepositories {@link WordRepository}, {@link WordDetailMainRepository}
     * @param dto {@link WordDto}
     * @param word {@link Word}
     * @return 저장된 단어 데이터 {@link Word}
     * */
    public static String updateAll(List<JpaRepository> jpaRepositories, WordDto.Request dto, Word word){
        WordRepository wordRepository = (WordRepository) jpaRepositories.get(0);
        WordDetailMainRepository wordDetailMainRepository = (WordDetailMainRepository) jpaRepositories.get(1);
        // 2 단어 업데이트
        //단어 테이블 컬럼 추가 시 아래 코드 작성 필요
        //word.setWord(dto.getWord());  //word는 업데이트 불필요(pk값)_20230701
        word.setMean(dto.getMean());
        word.setRead(dto.getRead());
        if (dto.getMemo() != null) word.setMemo(dto.getMemo());
        word.setNowTime();
        wordRepository.save(word);

        // 3. 디테일 데이터 업데이트
        List<WordDetailMain> existingWordDetailMains = word.getWordDetailMains();
        List<WordDetailMainDto.Request> inputWordDetailMainsMain = dto.getWordDetails();

        // 현재 디테일 갯수 확인
        for (int i = 0; i < existingWordDetailMains.size(); i++){
            WordDetailMain existingWordDetailMain = existingWordDetailMains.get(i);
            if (i < inputWordDetailMainsMain.size()){
                WordDetailMainDto.Request inputWordDetailMain = inputWordDetailMainsMain.get(i);
                existingWordDetailMain.setContent(inputWordDetailMain.getContent());
                existingWordDetailMain.setMemo(inputWordDetailMain.getMemo());
                wordDetailMainRepository.save(existingWordDetailMain);
            }else{  // 현재 디테일 갯수보다 적으면 delete 실행
                //해당하는 단어의 모든 디테일 데이터 삭제
                wordDetailMainRepository.deleteWordDetail(dto.getWord_id());
            }
        }

        // 현재 유의어 갯수보다 update할 유의어 갯수가 더 많으면 insert 실행
        for (int i = existingWordDetailMains.size(); i < inputWordDetailMainsMain.size(); i++) {
            WordDetailMainDto.Request inputWordDetailMain = inputWordDetailMainsMain.get(i);
            WordDetailMain newWordDetailMain = WordDetailMain.builder()
                    .content(inputWordDetailMain.getContent())
                    .memo(inputWordDetailMain.getMemo())
                    .word(word)
                    .wordDetailTitle(inputWordDetailMain.toEntity().getWordDetailTitle())
                    .build();
            wordDetailMainRepository.save(newWordDetailMain);
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
    * 메소드: saveWord(WordDto.Request dto)
    * @param dto {@link WordDto.Request}
    * @return Word {@link Word}
    * @note
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */

    @Override
    @Transactional
    public Word saveWord(WordDto.Request dto, String type){
        Word word = dto.toEntity();
        List<WordDetailMainDto.Request> inputWordDetailMainsMain = dto.getWordDetails();
//        List<WordDetailMain> newWordDetailMains = inputWordDetailMainsMain.stream()
//                .map(inputWordDetailMain -> inputWordDetailMain.toEntity(word, new WordDetailTitle(inputWordDetailMain.getTitle_id())))
//                .collect(Collectors.toList());
//
//        newWordDetailMains.forEach(wd -> wd.getWordDetailSub().forEach(wd::addWordDetailSub));
//
//        word.addWordDetailMain(newWordDetailMains);

        for (WordDetailMainDto.Request inputWordDetailMain : inputWordDetailMainsMain) {
            WordDetailMain wd = inputWordDetailMain.toEntity(word, new WordDetailTitle(inputWordDetailMain.getTitle_id()));
            List<WordDetailSub> ds = wd.getWordDetailSub();
//            for(int i=0; i<ds.size(); i++){
//                wd.addWordDetailSub(ds.get(i));
//            }
            for (WordDetailSub detailSub : ds) {
                wd.addWordDetailSub(detailSub);
            }
            word.addWordDetailMain(wd);
        }

        //folder_id 입력
        if (dto.getFolder_id() != null) {
            Folder folder = new Folder();
            folder.setFolderId(dto.getFolder_id());
            word.setFolder(folder);
        }

        word.setLang(GttsCode.valueOf(type));

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
    @Transactional
    public String createSoundFile(String wordName, String type){
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
           code = new ProcessBuilderUtil(wordName, fileName, GttsCode.valueOf(type).getTTS()).run();
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
    @Transactional
    public String removeByWord(WordDto.Request dto){
       Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));
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
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, read, memo);
        //return wordRepository.findById(word_id).get();
    }*/

    @Override
    public List<Word> getBySearchWord(String user_id, String data){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, read, memo);
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
        return wordRepository.getByAllWord(user_id);
    }

    @Override
    public List<Word> getByFolderWord(String user_id, int folder_id){
        return wordRepository.getByFolderWord(user_id, folder_id);
    }
}
