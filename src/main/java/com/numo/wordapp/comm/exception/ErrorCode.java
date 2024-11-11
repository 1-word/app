package com.numo.wordapp.comm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_AUTHORIZED(1000, "로그인이 필요합니다.", "로그인이 필요합니다."),
    UNRECOGNIZED_ROLE(1001, "에러가 발생했습니다. 잠시 후 시도해주세요.", "권한이 없습니다."),
    USER_NOT_FOUND(1002, "해당하는 유저를 찾을 수 없습니다.", "해당하는 유저를 찾을 수 없습니다."),
    LOGIN_ID_FAILED(1003, "아이디 또는 비밀번호가 올바르지 않습니다.", "아이디가 없습니다."),
    LOGIN_PW_FAILED(1004, "아이디 또는 비밀번호가 올바르지 않습니다.", "비밀번호가 맞지 않습니다"),

    PASSWORD_NOT_MATCHED(1005, "입력한 정보가 올바르지 않습니다.", "비밀번호가 맞지 않습니다."),

    DUPLICATED_ID(1006, "중복된 아이디입니다.", "중복된 아이디입니다."),
    WITHDRAWN_ACCOUNT(1007, "탈퇴한 계정입니다.", "탈퇴한 계정입니다."),

    EXPIRED_TOKEN(1008, "토큰이 만료되었습니다.", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(1009, "저장된 토큰 정보가 없습니다.", "저장된 토큰 정보가 없습니다."),

    DATA_NOT_FOUND(2000, "해당하는 데이터가 없습니다.", "해당하는 데이터가 없습니다."),
    TYPE_NOT_FOUND(2005, "에러가 발생했습니다. 잠시 후 시도해주세요.", "해당하는 타입이 없습니다."),
    ASSOCIATED_DATA_EXISTS(2010, "폴더 안에 데이터가 존재합니다. 이동 후 다시 시도해주세요.", "폴더 안에 데이터가 존재합니다."),
    ;

    private final int code;
    private final String description;
    private final String remark;
}
