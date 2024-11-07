package com.numo.wordapp.repository;

import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WordRepositoryCustom {
    /**
     * 해당하는 유저의 단어 데이터 조회, 폴더 아이디와 마지막 단어 아이디가 없는지 확인 후 동적으로 쿼리 생성
     * @param pageable 페이징 값, default 20
     * @param readDto <br>
     * * user_id 로그인 유저(토큰) 아이디 <br>
     * * folder_id 폴더 아이디 <br>
     * * last_word_id 마지막으로 출력된 단어 아이디 <br>
     * @return 페이징한 유저 데이터
     */
    Slice<Word> findByPageWord(Pageable pageable, WordDto.Read readDto);
}
