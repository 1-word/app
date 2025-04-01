package com.numo.api.domain.wordbook.word.aop;

import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.api.domain.dictionary.service.DictionaryCacheService;
import com.numo.api.domain.dictionary.service.DictionaryService;
import com.numo.api.domain.wordbook.sound.service.SoundService;
import com.numo.api.domain.wordbook.word.dto.WordResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect //AOP로 정의하는 클래스 지정
@Component  //빈 등록
public class WordAspect {

    private final DictionaryService dictionaryService;
    private final DictionaryCacheService dictionaryCacheService;
    private final SoundService soundService;

    public WordAspect(DictionaryService dictionaryService,
                      DictionaryCacheService dictionaryCacheService, SoundService soundService) {
        this.dictionaryService = dictionaryService;
        this.dictionaryCacheService = dictionaryCacheService;
        this.soundService = soundService;
    }

    /**
     * 단어 저장 시 사전 데이터베이스와 캐시에 저장
     * 이미 사전 데이터베이스에 있다면 저장하지 않는다.
     * @param res 저장된 단어 데이터
     */
    @AfterReturning(value = "execution(* com.numo.api..WordService.saveWord(..))", returning = "res")
    public void afterWordSave(WordResponseDto res) {
        DictionaryDto dictionaryDto = DictionaryDto.builder()
                .word(res.word())
                .build();

        DictionaryDto dict = dictionaryService.save(dictionaryDto);
        // 캐시 저장소에도 이미 사전 데이터베이스에 있거나, 제대로된 단어 데이터가 아니면 저장하지 않음
        log.debug("isSavedToCache: " + dict.isSavedToCache());
        if (dict.isSavedToCache()) {
            dictionaryCacheService.save("dict", res.word());
        }
    }
}
