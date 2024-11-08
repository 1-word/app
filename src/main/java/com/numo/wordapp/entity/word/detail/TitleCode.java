package com.numo.wordapp.entity.word.detail;

public enum TitleCode {
    M("M", "MAIN_TITLE"),
    S("S", "SUB_TITLE");

    private String type;
    private String remark;

    TitleCode(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }
}
