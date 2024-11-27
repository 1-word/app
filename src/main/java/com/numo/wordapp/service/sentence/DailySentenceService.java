package com.numo.wordapp.service.sentence;

import com.numo.wordapp.dto.sentence.*;
import com.numo.wordapp.entity.sentence.DailySentence;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.repository.sentence.DailySentenceRepository;
import com.numo.wordapp.service.word.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Word> dailyWords = findDailyWords(userId, requestDto.sentence());
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
     * 문장을 업데이트한다.
     * 문장이 변경되지 않으면 뜻만 수정
     * 문장이 변경되면 연관된 단어 모두 삭제 후 다시 등록
     *
     * @param userId 유저 아이디
     * @param dailySentenceId 수정할 문장 고유번호
     * @param requestDto 수정할 내용
     * @return 수정한 문장 데이터
     */
    @Transactional
    public DailySentenceDto updateSentence(Long userId, Long dailySentenceId, DailySentenceRequestDto requestDto) {
        DailySentence dailySentence = dailySentenceRepository.findDailySentenceBy(dailySentenceId, userId);

        if (requestDto.sentence() == null ||
                dailySentence.isCurrentSentenceEqual(requestDto.sentence())) {
            dailySentence.update(requestDto);
            return DailySentenceDto.of(dailySentence);
        }

        List<Word> dailyWords = findDailyWords(userId, requestDto.sentence());
        // 등록된 단어 데이터를 모두 삭제하고 새로 단어 데이터를 등록
        dailySentence.update(requestDto, dailyWords);
        return DailySentenceDto.of(dailySentence);
    }

    /**
     * 문장에 해당하는 단어를 찾아 내 단어장에 등록된 단어 데이터가 있는지 확인
     * @param userId 유저 아이디
     * @param sentence 문장
     * @return 단어장에 등록된 문장과 연관된 단어 데이터
     */
    private List<Word> findDailyWords(Long userId, String sentence){
        // 단어를 쪼갠다
        List<String> words = splitSentence(sentence);

        List<DailyWordDto> dailyWordsDto = wordService.findDailyWord(userId, words);
        List<Word> dailyWords = dailyWordsDto.stream().map(
                        dailyWordDto -> Word.builder().wordId(dailyWordDto.wordId()).build())
                .toList();
        return dailyWords;
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
