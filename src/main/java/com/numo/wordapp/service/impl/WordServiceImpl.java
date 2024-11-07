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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
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
            public String updateByWordType(WordDto.Request dto, Word word){
                return "updateAll";
            }
        },
        memo{
            @Override
            public String updateByWordType(WordDto.Request dto, Word word){
                return "updateMemo";
            }
        },
        memorization{
            @Override
            public String updateByWordType(WordDto.Request dto, Word word) {
                return "updateMemorization";
            }
        },
        wordFolder{
            @Override
            public String updateByWordType(WordDto.Request dto, Word word) {
                return "updateWordFolder";
            }
        };

        public abstract String updateByWordType(WordDto.Request dto, Word word);
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
    public Word updateByWord(WordDto.Request dto, String type){
        Word result = null;
        // 1. 해당 단어 유효한지 검색
        Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));  //db에서 조회를 하면 영속성 유지..
        try{
            // 2. 업데이트 타입 확인
            UpdateType updateType = UpdateType.valueOf(type);
            String methodName = updateType.updateByWordType(dto, word);
            // 3. 타입에 알맞는 함수 실행
//            Class c = Class.forName("com.numo.wordapp.service.impl.WordServiceImpl");
//            c.getMethod(methodName, WordDto.Request.class, Word.class).invoke(this, dto, word);

//            Method m = c.getMethod(methodName, WordDto.Request.class, Word.class);
//            Method m = c.getDeclaredMethod(methodName);
            Method m = this.getClass().getDeclaredMethod(methodName, WordDto.Request.class, Word.class);
            m.setAccessible(true);  // private 메서드 접근
            result = (Word) m.invoke(this, dto, word);
        } catch (Exception e){
            throw new CustomException(ErrorCode.TypeNotFound);
        }

        return result;
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
    public Word updateAll(WordDto.Request dto, Word word){
        // 1. 단어 업데이트
        // 단어 테이블 컬럼 추가 시 아래 코드 작성 필요
        // word.setWord(dto.getWord());  //word는 업데이트 불필요(pk값)_20230701
        word.setMean(dto.getMean());
        word.setRead(dto.getRead());
        if (dto.getMemo() != null) word.setMemo(dto.getMemo());
        word.setUpdateTimeNow();

        // 2. 원래 있는 데이터는 그대로 업데이트 하되
        // 원래 있는 데이터보다 많아지면 add해야함.

        // 현재 케이스..
        // 1. 데이터베이스에 저장된 갯수보다 수정한 데이터 갯수가 더 많음
        // word에 addDetail..
        // 2. 데이터베이스에 저장된 갯수보다 수정한 데이터 갯수가 더 적음
        // 2-1. 현재 있는 데이터를 전부 삭제 후 다시 입력할 것인가?
        // 2-2. 수정한 데이터 갯수만 남긴 뒤 setter로 수정할 것인가? V

        // 3. dto에서는 관련 id값이 없음.. word에서 가져와야함!
        // dto => word
        int detailMainMax = word.getWordDetailMains().size();
        int detailMainDtoMax = dto.getDetails().size();
        // 원래있는 데이터보다 업데이트 할 데이터가 적을 때 데이터 삭제를 위한 max값 계산.
        int max = Math.max(detailMainMax, detailMainDtoMax);
        int removeIdx = 0;
        for (int i=0; i<max; i++){
            // 데이터베이스(word)에 저장된 데이터 갯수보다, 현재 update할 데이터(dto) 갯수가 더 적으면 삭제해줌
            if (detailMainMax > detailMainDtoMax && i > detailMainDtoMax-1){
                // detailSub도 같이 삭제가 되어야함
                // 이 조건일 때는 무조건 데이터베이스에 저장된 갯수가 더 많을 때 발생함.
                int index = i-removeIdx;    // 삭제가 되므로 index를 조정해주어야 함
                int detailSubMax = word.getWordDetailMains().get(index).getWordDetailSub().size();
                // detailSub 삭제: 안해주면 데이터베이스에서 삭제가 안됨
                for (int j=0; j<detailSubMax; j++){
                    word.getWordDetailMains().get(index).getWordDetailSub().remove(word.getWordDetailMains().get(index).getWordDetailSub().get(j));
                }
                // remove때문에 maxLength가 2였다면 1로 변경이 되어, outOfIndex Error 발생
                // detailMain 삭제
                word.getWordDetailMains().remove(word.getWordDetailMains().get(index));
                removeIdx += 1;
                continue;
            }

            WordDetailMainDto.Request wdDto = dto.getDetails().get(i);

            // 원래 있는 데이터보다 많으면 add 해줌
            if (i > detailMainMax-1){
                WordDetailMain wd = wdDto.toEntity(word, new WordDetailTitle(wdDto.getTitle_id()));
                List<WordDetailSub> ds = wd.getWordDetailSub();
                for (WordDetailSub detailSub : ds) {
                    wd.addWordDetailSub(detailSub);
                }
                word.addWordDetailMain(wd);
                continue;
            }

            // 원래 데이터와 크기가 같다면 값만 업데이트
            word.getWordDetailMains().get(i).setContent(wdDto.getContent());
            word.getWordDetailMains().get(i).setMemo(wdDto.getMemo());
            word.getWordDetailMains().get(i).setUpdateTimeNow();

            // 이제 detailSub도 업데이트 해야함...!!
            // 동일한 구조인데... 어떻게 함수로 쓸 방법 없나?
            int detailSubIndex = 0;
            for (WordDetailSubDto.Request wordDetailSubDto : wdDto.getSubs()){
                word.getWordDetailMains().get(i).getWordDetailSub().get(detailSubIndex).setContent(wordDetailSubDto.getContent());
                word.getWordDetailMains().get(i).getWordDetailSub().get(detailSubIndex).setMemo(wordDetailSubDto.getMemo());
                word.getWordDetailMains().get(i).getWordDetailSub().get(detailSubIndex).setUpdateTimeNow();
                detailSubIndex += 1;
            }
        }
        // 영속성을 갖는 객체를 수정하면 데이터 베이스에 반영이 되기 때문에 save()는 update에서 사용하지 않는다.
        return word;
    }

    @Transactional
    public Word updateMemo(WordDto.Request dto, Word word){
        word.setMemo(dto.getMemo());
        return word;
    }

    @Transactional
    public Word updateMemorization(WordDto.Request dto, Word word){
        word.setMemorization(dto.getMemorization());
        return word;
    }

    private Word updateWordFolder(WordDto.Request dto, Word word){
        Folder folder = new Folder();
        folder.setFolderId(dto.getFolder_id());
        word.setFolder(folder);
        return wordRepository.save(word);
    }

    /**
    * 단어 저장
    * @param dto {@link WordDto.Request}
    * @return Word {@link Word}
    * @note
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */
    @Override
    @Transactional
    public Word saveWord(WordDto.Request dto, String gttsType){
        Word word = dto.toEntity();
        List<WordDetailMainDto.Request> inputWordDetailMainsMain = dto.getDetails();

        for (WordDetailMainDto.Request inputWordDetailMain : inputWordDetailMainsMain) {
            WordDetailMain wd = inputWordDetailMain.toEntity(word, new WordDetailTitle(inputWordDetailMain.getTitle_id()));
            List<WordDetailSub> ds = wd.getWordDetailSub();
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

        word.setLang(GttsCode.valueOf(gttsType));

        Optional<String> soundPathOptional = soundRepository.findByWord(word.getWord())
                .map(sound -> sound.getSoundPath());

        String fileName = soundPathOptional.orElse(null);
        if(fileName == null || fileName == ""){ // 해당 하는 단어의 sound가 없으면 sound파일 생성 및 데이터베이스에 추가
            fileName = createSoundFile(word.getWord(), gttsType);
        }
        word.setSoundPath(fileName);
        word.setMemorization("N");
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

    /**
     * word 데이터 삭제
    * @param dto {@link WordDto.Request}
    * @return 삭제한 완료 메시지
    * */

    @Override
    @Transactional
    public int removeByWord(WordDto.Request dto){
       Word word = wordRepository.findByUserIdAndWordId(dto.getUser_id(), dto.getWord_id()).orElseThrow(() -> new CustomException(ErrorCode.DataNotFound));
       wordRepository.delete(word);
        return dto.getWord_id();
    }

    /**
     * 검색한 단어(Word) 및 유의어(Synonym) 조회
     * @param readDto <br>
     * * user_id 로그인 사용자(토큰) 아이디 <br>
     * * search_text 검색 데이터 <br>
     * @return 검색한 Word 데이터
     * @apiNote 양방향 참조이기때문에 DTO를 이용해서 리턴해야함 (무한 참조 발생 가능)
     */
    @Override
    public Slice<Word> getBySearchWord(WordDto.Read readDto){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, read, memo);
        //return wordRepository.findById(word_id).get();
        return wordRepository.findByPageWord(PageRequest.of(readDto.getPage(), 20), readDto);
    }

    /**
     * 페이징 처리하지 않은 모든 단어 조회
     * @param user_id 로그인 사용자(토큰) 아이디
     * @return 해당하는 사용자의 모든 단어 데이터 리턴
     */
    @Override
    public List<Word> getByAllWord(String user_id){
        return wordRepository.getByAllWord(user_id);
    }

    /**
     * 폴더에 있는 모든 단어 데이터 조회 (페이징x)
     * @param user_id
     * @param folder_id
     * @return 해당하는 폴더에 있는 모든 단어 데이터 리턴
     */
    @Override
    public List<Word> getByFolderWord(String user_id, int folder_id){
        return wordRepository.getByFolderWord(user_id, folder_id);
    }

    /**
     * 단어를 지정한 수(20개씩) 끊어서 조회. (No-Offset)
     * @param page 현재 페이지
     * @param readDto <br>
     * * user_id 로그인 유저(토큰) 아이디 <br>
     * * folder_id 폴더 아이디 <br>
     * * last_word_id 마지막으로 출력된 단어 아이디 <br>
     * @return 조회한 단어 및 페이징 값
     */
    @Override
    public Slice<Word> getByPagingWord(int page, WordDto.Read readDto){
        return wordRepository.findByPageWord(PageRequest.of(page, 20), readDto);
    }

}
