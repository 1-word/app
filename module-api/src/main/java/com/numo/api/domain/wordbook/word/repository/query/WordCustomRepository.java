package com.numo.api.domain.wordbook.word.repository.query;

import com.numo.api.domain.wordbook.folder.dto.read.FolderInWordCountDto;
import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.domain.wordbook.word.dto.WordDto;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface WordCustomRepository {
    /**
     * 해당하는 유저의 단어 데이터 조회, 폴더 아이디와 마지막 단어 아이디가 없는지 확인 후 동적으로 쿼리 생성
     *
     * @param pageable 페이징 값, default 20
     * @param readDto  <br>
     */
    Slice<WordDto> findWordBy(Pageable pageable, Long userId, Long lastWordId, ReadWordRequestDto readDto);
    DailyWordListDto findDailyWordBy(Long userId, List<String> words);

    WordDto findWordByWordId(Long userId, Long wordId);

    List<WordDetailResponseDto> findWordDetailByIds(List<Long> wordIds);
}
