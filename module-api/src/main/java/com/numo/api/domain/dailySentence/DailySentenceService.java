package com.numo.api.domain.dailySentence;

import com.numo.domain.sentence.DailySentence;
import com.numo.domain.sentence.dto.CreateWordDailySentenceDto;
import com.numo.domain.sentence.dto.DailySentenceRequestDto;
import com.numo.domain.wordbook.word.Word;
import com.numo.api.domain.dailySentence.dto.DailySentenceDto;
import com.numo.api.domain.dailySentence.dto.DailyWordDto;
import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailySentenceDto;
import com.numo.api.domain.dailySentence.dto.read.ReadDailyWordDto;
import com.numo.api.global.comm.date.DateRequestDto;
import com.numo.api.domain.dailySentence.dto.wordDailySentence.DailyWordDetailDto;
import com.numo.api.domain.dailySentence.dto.wordDailySentence.WordDailySentenceDto;
import com.numo.api.domain.dailySentence.repository.DailySentenceRepository;
import com.numo.api.domain.wordbook.word.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        List<CreateWordDailySentenceDto> dailyWords = findDailyWords(userId, requestDto.sentence());
        sentence.setWordDailySentence(dailyWords);
        List<String> words = dailyWords.stream().map(CreateWordDailySentenceDto::matchedWord).toList();
        sentence.setTagSentence(addTag(words, requestDto.sentence()));
        return DailySentenceDto.of(dailySentenceRepository.save(sentence));
    }

    /**
     * 연도, 월, 주, 일별 문장 데이터를 가져온다.
     * @param userId 유저 아이디
     * @param parameterDto 년도, 달, 월, 주
     * @return 연도, 월, 주, 일별 문장 데이터
     */
    public List<ReadDailySentenceDto> getSentenceBy(Long userId, DateRequestDto parameterDto) {
        List<DailySentenceDto> dailySentences = dailySentenceRepository.findDailySentencesBy(userId, parameterDto);
        List<Long> sentenceIds = dailySentences.stream().map(DailySentenceDto::dailySentenceId).toList();
        List<ReadDailyWordDto> dailyWords = dailySentenceRepository.findDailyWordsBy(sentenceIds);

        Map<Long, List<ReadDailyWordDto>> dailyWordsMap = dailyWords.stream()
                .collect(Collectors.groupingBy(ReadDailyWordDto::wordDailyWordId));

        List<ReadDailySentenceDto> res = dailySentences.stream()
                .map(dailySentenceDto -> ReadDailySentenceDto.of(dailySentenceDto,
                        dailyWordsMap.get(dailySentenceDto.dailySentenceId()))
                ).toList();

        return res;
    }

    /**
     * 문장을 업데이트한다.
     * 문장이 변경되지 않으면 뜻만 수정
     * 문장이 변경되면 연관된 단어 모두 삭제 후 다시 등록
     *
     * @param userId 유저 아이디
     * @param dailySentenceId 문장 고유번호
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

        List<CreateWordDailySentenceDto> dailyWords = findDailyWords(userId, requestDto.sentence());

        List<String> words = dailyWords.stream().map(CreateWordDailySentenceDto::matchedWord).toList();

        String tagSentence = addTag(words, requestDto.sentence());
        // 등록된 단어 데이터를 모두 삭제하고 새로 단어 데이터를 등록
        dailySentence.update(requestDto, dailyWords, tagSentence);
        return DailySentenceDto.of(dailySentence);
    }

    /**
     * 연관된 오늘의 단어 데이터와 함께 문장을 삭제한다
     * @param userId 유저 아이디
     * @param dailySentenceId 문장 고유번호
     */
    public void deleteSentence(Long userId, Long dailySentenceId) {
        DailySentence dailySentence = dailySentenceRepository.findDailySentenceBy(dailySentenceId, userId);
        dailySentence.removeDailyWords();
        dailySentenceRepository.delete(dailySentence);
    }

    public List<Integer> getSentenceDaysByYearAndMonth(Long userId, DateRequestDto parameterDto) {
        return dailySentenceRepository.findDailySentenceDays(userId, parameterDto);
    }

    /**
     * 연관 단어 정보 리스트를 조회한다
     * @param userId 유저 아이디
     * @param wordDailySentenceId 조회할 오늘의 내 문장 고유 번호
     * @return 연관 단어 정보 리스트
     */
    public List<WordDailySentenceDto> getWordDailySentenceInfo(Long userId, Long wordDailySentenceId) {
        return dailySentenceRepository.getWordDailySentenceInfo(userId, wordDailySentenceId);
    }

    /**
     * 문장에 해당하는 단어를 찾아 내 단어장에 등록된 단어 데이터가 있는지 확인
     * @param userId 유저 아이디
     * @param sentence 문장 문자열
     * @return 단어장에 등록된 문장과 연관된 단어 데이터
     */
    private List<CreateWordDailySentenceDto> findDailyWords(Long userId, String sentence){
        List<String> words = splitSentence(sentence);
        DailyWordListDto dailyWordListDto = wordService.findDailyWord(userId, words);

        // 매칭되는 문자열을 확인하기 위해 체크
        List<DailyWordDto> detailDtos = matchSearchWordWithDailyWord(dailyWordListDto.detailDtos(), words);
        List<DailyWordDto> wordDtos = dailyWordListDto.wordDtos();

        wordDtos.addAll(detailDtos);

        // 중복 체크
        List<DailyWordDto> list = wordDtos.stream().distinct().toList();

        List<CreateWordDailySentenceDto> results = list.stream()
                .map(dailyWordDto -> new CreateWordDailySentenceDto(
                        new Word(dailyWordDto.wordId()),
                        dailyWordDto.word())
                ).toList();

        return results;
    }

    /**
     * 매칭되는 문자열을 저장하기 위해 상세 정보 필드 중 어떤 필드가 연관 단어와 일치하는지 확인
     * @param list 상세정보 리스트
     * @param searchWords 연관 단어 리스트
     * @return 단어 고유번호와 매칭 문자열 정보를 가지고 있는 객체
     */
    private List<DailyWordDto> matchSearchWordWithDailyWord(List<DailyWordDetailDto> list, List<String> searchWords) {
        List<String> newSearchWords = new ArrayList<>();
        newSearchWords.addAll(searchWords);

        List<DailyWordDto> results = list.stream().map(dailyWordDto -> {
            Iterator<String> iterator = newSearchWords.iterator();
            DailyWordDto result = null;
            while (iterator.hasNext()) {
                String current = iterator.next();
                if (Objects.equals(current, dailyWordDto.title())) {
                    iterator.remove();
                    result = new DailyWordDto(dailyWordDto.wordId(), dailyWordDto.title());
                    break;
                } else if (Objects.equals(current, dailyWordDto.content())) {
                    iterator.remove();
                    result = new DailyWordDto(dailyWordDto.wordId(), dailyWordDto.content());
                    break;
                }
            }
            return result;
        }).toList();

        return results;
    }

    /**
     * 문장에서 연관 단어 표시를 위해 문장에 태그를 추가한다.
     * @param words 연관 단어 리스트
     * @param sentence 문장
     * @return 연관 단어에 태그가 추가된 문장 문자열
     */
    private String addTag(List<String> words, String sentence) {
        String result = sentence;

        for (String word : words) {
            // -로 붙어있는 문자열은 같은 단어로 취급하지 않고 전체가 동일해야 한다.
            String regex = "(?<!-)\\b" + Pattern.quote(word) + "\\b(?!-)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result);

            result = matcher.replaceAll("<strong>" + word + "</strong>");
        }

        return result;
    }

    /**
     * 공백을 기준으로 문장을 분리한다.
     * @param sentence 문장
     * @return 공백을 기준으로 분리된 단어들
     */
    private List<String> splitSentence(String sentence) {
        String newSentence = sentence.replaceAll("[.,\"'\\[\\]{}]+", "");
        String[] words = newSentence.split(" ");
        return Arrays.stream(words).toList();
    }
}
