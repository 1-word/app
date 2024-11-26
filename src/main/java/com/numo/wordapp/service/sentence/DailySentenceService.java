package com.numo.wordapp.service.sentence;

import com.numo.wordapp.dto.sentence.DailySentenceDto;
import com.numo.wordapp.dto.sentence.DailySentenceRequestDto;
import com.numo.wordapp.dto.word.DailyWordDto;
import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.repository.sentence.DailySentenceRepository;
import com.numo.wordapp.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailySentenceService {
    private final DailySentenceRepository dailySentenceRepository;
    private final WordService wordService;

    /**
     * 문장 입력 시 문장을 스페이스바를 기준으로 나누어 단어로 쪼갠 뒤
     * 해당하는 단어가 자신의 단어장에 저장되어 있다면 정보를 저장함
     * @param userId 유저 아이디
     * @param requestDto 저장할 문장
     * @return 자신의 단어장에 저장되어 있는 정보가 포함된 저장한 문장 데이터
     */
    public DailySentenceDto saveSentence(Long userId, DailySentenceRequestDto requestDto) {
        DailySentence sentence = requestDto.toEntity(userId);
        // 단어를 쪼갠다
        List<String> words = splitSentence(requestDto.sentence());

        List<DailyWordDto> dailyWordsDto = wordService.findDailyWord(userId, words);
        List<Word> dailyWords = dailyWordsDto.stream().map(
                dailyWordDto -> Word.builder().wordId(dailyWordDto.wordId()).build())
                .toList();
        sentence.setWordDailySentence(dailyWords);
        return DailySentenceDto.of(dailySentenceRepository.save(sentence), dailyWordsDto);
    }

    private List<String> splitSentence(String sentence) {
        String[] words = sentence.split(" ");
        return Arrays.stream(words).toList();
    }

}
