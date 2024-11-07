package com.numo.wordapp.model.word;

/**
 * 단어 언어 코드 설정(EN, JP, KO)
 * <pre>사용 예시: GttsCode.valueOf(type)</pre>
 */
public enum GttsCode {
    EN("EN", "en"), JP("JP", "ja"), KO("KO", "ko");
    private String WORD;
    private String TTS;

    GttsCode(String WORD, String TTS) {
        this.WORD = WORD;
        this.TTS = TTS;
    }

    public String getValue() {
        return this.WORD;
    }

    public void setValue(String wordType){
        this.WORD = wordType;
    }

    public String getTTS(){
        return this.TTS;
    }

    public void setTTS(String TTS) {
        this.TTS = TTS;
    }

    public String getKey(){
        return name();
    }
}
