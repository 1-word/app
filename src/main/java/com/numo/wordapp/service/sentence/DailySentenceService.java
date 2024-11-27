package com.numo.wordapp.service.sentence;

import com.numo.wordapp.dto.sentence.*;
import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.repository.sentence.DailySentenceRepository;
import com.numo.wordapp.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return DailySentenceDto.of(dailySentenceRepository.save(sentence));
    }

    /**
     * 연도, 월, 주, 일별 문장 데이터를 가져온다.
     * @param userId 유저 아이디
     * @param parameterDto 년도, 달, 월, 주
     * @return 연도, 월, 주, 일별 문장 데이터
     */
    public List<ReadDailySentenceDto> getSentenceBy(Long userId, DailySentenceParameterDto parameterDto) {
        List<DailySentenceDto> dailySentences = dailySentenceRepository.findDailySentencesBy(userId, parameterDto);
        List<Long> sentenceIds = dailySentences.stream().map(DailySentenceDto::dailySentenceId).toList();
        List<DailyWordDto> dailyWords = dailySentenceRepository.findDailyWordsBy(sentenceIds);

        List<ReadDailySentenceDto> res = dailySentences.stream()
                .map(dailySentenceDto -> ReadDailySentenceDto.of(dailySentenceDto,
                        findDailySentenceWords(dailySentenceDto.dailySentenceId(), dailyWords))
                ).toList();

        return res;
    }

    /**
     * 가져온 단어 데이터에서 해당 문장과 연관된 단어를 찾는다.
     * @param sentenceId 문장
     * @param dailyWordDtos 검색한 단어 데이터
     * @return 해당 문장과 연관된 단어 데이터
     */
    private List<DailyWordDto> findDailySentenceWords(Long sentenceId, List<DailyWordDto> dailyWordDtos) {
        List<DailyWordDto> result = new ArrayList<>();
        Iterator<DailyWordDto> iterator = dailyWordDtos.iterator();
        while (iterator.hasNext()) {
            DailyWordDto dailyWordDto = iterator.next();
            if (Objects.equals(sentenceId, dailyWordDto.wordDailyWordId())) {
                result.add(dailyWordDto);
                iterator.remove();
            }
        }
        return result;
    }

    /**
     * 공백을 기준으로 문장을 분리한다.
     * @param sentence 문장
     * @return 공백을 기준으로 분리된 단어들
     */
    private List<String> splitSentence(String sentence) {
        String[] words = sentence.split(" ");
        return Arrays.stream(words).toList();
    }
}
