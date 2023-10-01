package com.numo.wordapp.security.model;

public enum WordType {
    EN("EN", "en"), JP("JP", "ja"), KO("KO", "ja");
    private String wordType;
    private String ttsType;

    WordType(String wordType, String ttsType) {
        this.wordType = wordType;
        this.ttsType = ttsType;
    }

    public String getValue() {
        return this.wordType;
    }

    public void setValue(String wordType){
        this.wordType = wordType;
    }

    public String getTtsType(){
        return this.ttsType;
    }

    public void setTtsType(String ttsType) {
        this.ttsType = ttsType;
    }

    public String getKey(){
        return name();
    }
}
