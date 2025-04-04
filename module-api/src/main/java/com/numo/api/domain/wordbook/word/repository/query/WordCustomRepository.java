package com.numo.api.domain.wordbook.word.repository.query;

import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import com.numo.api.domain.wordbook.word.dto.WordDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface WordCustomRepository {
    Slice<WordDto> findWordBy(Long wordBookId, Pageable pageable, Long lastWordId, ReadWordRequestDto readDto);
    DailyWordListDto findDailyWordBy(Long userId, List<String> words);

    WordDto findWordByWordId(Long wordId);

    List<WordDetailResponseDto> findWordDetailByWordIds(List<Long> wordIds);
}
