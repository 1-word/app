package com.numo.api.domain.wordbook.sound.service;

import com.numo.api.domain.wordbook.sound.repository.SoundRepository;
import com.numo.api.global.comm.gtts.Gtts;
import com.numo.api.global.comm.gtts.GttsService;
import com.numo.api.global.conf.PropertyConfig;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoundService {
    private final SoundRepository soundRepository;
    private final GttsService gttsService;
    private final String path;

    public SoundService(SoundRepository soundRepository, GttsService gttsService, PropertyConfig propertyConfig) {
        this.soundRepository = soundRepository;
        this.gttsService = gttsService;
        this.path = propertyConfig.getGttsPath();
    }

    /**
     * 해당하는 단어의 음성파일이 없으면 파일 생성 및 데이터베이스에 해당하는 파일명을 저장한다.
     *
     * @param wordName 단어명
     */
    @Transactional
    public Sound createSound(String wordName, GttsCode gttsType) {
        Sound sound = soundRepository.findByWord(wordName);
        if (sound != null) {
            return sound;
        }

        createSoundFile(wordName, gttsType);
        // sound 테이블에 데이터 insert
        sound = Sound.builder()
                .word(wordName)
                .build();
        return soundRepository.save(sound);
    }

    public void createSoundFile(String wordName, GttsCode gttsType) {
        String savePath = path + "/" + wordName + ".mp3";
        Gtts gtts = new Gtts(wordName, gttsType.name().toLowerCase(), savePath);

        gttsService.saveAudio(gtts);
    }
}
