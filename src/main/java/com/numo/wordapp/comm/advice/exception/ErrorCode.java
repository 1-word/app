package com.numo.wordapp.comm.advice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OperationNotAuthorized(6000, "로그인이 필요합니다.", "로그인이 필요합니다."),
    DuplicatedIdFound(6001, "중복된 아이디입니다.", "중복된 아이디입니다."),
    UserNotFound(6002, "아이디 또는 비밀번호가 맞지 않습니다.", "해당하는 유저가 없습니다."),
    ExpiredToken(6004, "다시 로그인해주세요.", "토큰이 만료되었습니다."),
    RefreshTokenNotFound(6004, "다시 로그인해주세요.", "저장된 토큰 정보가 없습니다."),
    RefreshTokenNotEqual(6004, "다시 로그인해주세요.", "저장된 리프래쉬 토큰 정보가 일치하지 않습니다."),
    UnrecognizedRole(6005, "에러가 발생했습니다. 잠시 후 시도해주세요.", "권한이 없습니다."),
    TypeNotFound(6005, "에러가 발생했습니다. 잠시 후 시도해주세요.", "해당하는 타입이 없습니다."),
    BadCredentials(6006, "아이디 또는 비밀번호가 맞지 않습니다.", "자격 증명에 실패하였습니다."),
    LoginIDFail(6006, "아이디 또는 비밀번호가 맞지 않습니다.", "아이디가 맞지 않습니다."),
    LoginPWFail(6006, "아이디 또는 비밀번호가 맞지 않습니다.", "비밀번호가 맞지 않습니다."),
    DataNotFound(6007, "해당하는 데이터가 없습니다.", "해당하는 데이터가 없습니다."),
    AssociatedDataExists(6007, "폴더 안에 데이터가 존재합니다. 이동 후 다시 시도해주세요.", "폴더 안에 데이터가 존재합니다.");

    private int code;
    private String description;
    private String remark;
}
