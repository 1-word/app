package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.word.ReadWordRequestDto;
import com.numo.wordapp.entity.word.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface WordRepositoryCustom {
    /**
     * 해당하는 유저의 단어 데이터 조회, 폴더 아이디와 마지막 단어 아이디가 없는지 확인 후 동적으로 쿼리 생성
     * @param pageable 페이징 값, default 20
     * @param readDto <br>
     *
     * */
    Slice<Word> findWordBy(Pageable pageable, Long userId, ReadWordRequestDto readDto);
}
