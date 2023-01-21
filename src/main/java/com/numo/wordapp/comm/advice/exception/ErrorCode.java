package com.numo.wordapp.comm.advice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OperationNotAuthorized(6000, "자격 증명에 실패하였습니다."),
    DuplicatedIdFound(6001, "중복된 아이디입니다."),
    UserNotFound(6002, "해당하는 유저가 없습니다."),
    ExpiredToken(6003, "세션이 만료되었습니다. 다시 로그인해주세요."),
    rfTokenNotFound(6004, "저장된 토큰 정보가 없습니다. 다시 로그인해주세요."),
    UnrecongizedRole(6005, "권한이 없습니다."),
    LoginFail(6006, "아이디 또는 비밀번호가 맞지 않습니다.");


    private int code;
    private String description;
}
