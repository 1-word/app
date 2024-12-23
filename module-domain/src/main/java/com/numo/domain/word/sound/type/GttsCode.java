package com.numo.domain.word.sound.type;

import lombok.Getter;

/**
 * 단어 언어 코드 설정(EN, JP, KO)
 * <pre>사용 예시: GttsCode.valueOf(type)</pre>
 */
@Getter
public enum GttsCode {
    EN( "en"), JP("ja"), KO( "ko");
    private final String TTS;

    GttsCode(String TTS) {
        this.TTS = TTS;
    }
}
