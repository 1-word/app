package com.numo.wordapp.service;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.word.Word;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordService {
    /**
     * type{@link com.numo.wordapp.service.impl.WordServiceImpl.UpdateType}에 맞는 데이터 수정
     * @param dto
     * @param type
     * @return 업데이트 완료한 데이터
     */
    @Transactional
    Word updateByWord(WordDto.Request dto, String type);

    /**
     * 단어 저장
     * @param dto {@link WordDto.Request}
     * @return Word {@link Word}
     * */
    @Transactional
    Word saveWord(WordDto.Request dto, String gttsType);

    /**
     * word 데이터 삭제
     * @param dto {@link WordDto.Request}
     * @return 삭제 완료한 단어 아이디
     * */
    int removeByWord(WordDto.Request dto);

    //Word getBySearchWord(int word_id);

    /**
     * 검색한 단어(Word) 및 유의어(Synonym) 조회
     * @param readDto {@link WordDto.Read} 검색할 데이터
     * * page 현재 페이지 번호
     * * user_id 로그인 사용자(토큰) 아이디
     * * search_text 검색 데이터
     * @return 검색한 Word 데이터
     */
    Slice<Word> getBySearchWord(WordDto.Read readDto);


    /**
     * 페이징 처리 하지 않은 해당하는 유저 아이디의 단어 데이터를 가져온다.
     * @param user_id 로그인 사용자(토큰) 아이디
     * @return 페이징 처리 하지 않은 모든 단어 데이터
     */
    List<Word> getByAllWord(String user_id);

    List<Word> getByFolderWord(String user_id, int folder_id);

    /**
     * 단어를 지정한 수(20개씩) 끊어서 조회. (No-Offset)
     * @param page 현재 페이지
     * @param readDto <br>
     * * user_id 로그인 유저(토큰) 아이디 <br>
     * * folder_id 폴더 아이디 <br>
     * * last_word_id 마지막으로 출력된 단어 아이디 <br>
     * @return 조회한 단어 및 페이징 값
     */
    Slice<Word> getByPagingWord(int page, WordDto.Read readDto);

}
