package com.numo.api.comm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_AUTHORIZED(1000, "로그인이 필요합니다.", "로그인이 필요합니다."),
    UNRECOGNIZED_ROLE(1001, "권한이 없습니다.", "권한이 없습니다."),
    USER_NOT_FOUND(1002, "해당하는 유저를 찾을 수 없습니다.", "해당하는 유저를 찾을 수 없습니다."),
    LOGIN_ID_FAILED(1003, "아이디 또는 비밀번호가 올바르지 않습니다.", "아이디가 없습니다."),
    LOGIN_PW_FAILED(1004, "아이디 또는 비밀번호가 올바르지 않습니다.", "비밀번호가 맞지 않습니다"),

    PASSWORD_NOT_MATCHED(1005, "입력한 정보가 올바르지 않습니다.", "비밀번호가 맞지 않습니다."),

    DUPLICATED_ID(1006, "중복된 아이디입니다.", "중복된 아이디입니다."),
    WITHDRAWN_ACCOUNT(1007, "탈퇴한 계정입니다.", "탈퇴한 계정입니다."),

    EXPIRED_TOKEN(1008, "토큰이 만료되었습니다.", "토큰이 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(1009, "저장된 토큰 정보가 없습니다.", "저장된 토큰 정보가 없습니다."),

    OAUTH2_EMAIL_EXISTS(1010, "해당하는 유저 이메일이 다른 서비스에 이미 등록되어있습니다. 다른 아이디로 시도해주세요.", "해당하는 유저 이메일이 이미 등록되어있습니다."),
    OAUTH2_EMAIL_NULL(1011, "등록할 이메일이 없어 회원가입에 실패했습니다. 다른 아이디로 시도하거나 정보 변경 후 다시 시도해주세요.", "해당하는 유저 이메일이 없습니다."),

    INVALID_VERIFICATION_CODE(1012, "잘못된 인증번호이거나 만료된 인증번호입니다.", "잘못된 인증번호입니다."),
    UNVERIFIED_EMAIL(1013, "인증되지 않은 이메일입니다.", "인증되지 않은 이메일입니다."),
    SIGNUP_FAILED(1014, "인증되지 않은 이메일이거나 이미 가입된 이메일입니다.", "인증되지 않은 이메일이거나 가입된 이메일입니다."),
    ALREADY_VERIFIED_ACCOUNT(1015, "이미 인증된 계정입니다.", "이미 인증된 계정입니다"),

    CHANGE_PASSWORD_FAILED(1020, "인증되지 않아 비밀번호 변경에 실패했습니다. 인증 후 다시 시도해주세요.", "인증되지 않아 비밀번호 변경에 실패했습니다."),

    DATA_NOT_FOUND(2000, "해당하는 데이터가 없습니다.", "해당하는 데이터가 없습니다."),
    TYPE_NOT_FOUND(2005, "에러가 발생했습니다. 잠시 후 시도해주세요.", "해당하는 타입이 없습니다."),
    ASSOCIATED_DATA_EXISTS(2010, "폴더 안에 데이터가 존재합니다. 이동 후 다시 시도해주세요.", "폴더 안에 데이터가 존재합니다."),
    FILE_WRITE_FAILED(3000, "파일을 생성하던 중 오류가 발생했습니다.", "파일 생성 실패"),
    FILE_READ_FAILED(3001, "파일을 읽던 중 오류가 발생했습니다.", "파일 읽기 실패"),
    FILE_NOT_FOUND(3002, "해당하는 파일을 찾을 수 없습니다.", "해당하는 파일 아이디가 없습니다."),
    FILE_ACCESS_DENIED(3003, "파일에 접근권한이 없습니다.", "파일에 접근권한이 없습니다."),
    FOLDER_NOT_FOUND(3004, "해당하는 폴더가 없습니다.", "해당하는 폴더가 없습니다."),

    SOUND_CANNOT_CREATED(4000, "사운드 파일 생성 중 오류가 발생했습니다.", "사운드 파일 생성 중 오류가 발생했습니다."),
    WORD_GROUP_EXISTS(3050, "해당하는 품사명이 이미 존재하므로 저장할 수 없습니다. 다른 이름으로 시도해주세요.", "해당 계정에 동일한 품사명이 존재하므로 저장이 불가합니다."),
    WORD_GROUP_DELETE_FAILED(3051, "해당하는 품사에 데이터가 있어 삭제에 실패했습니다.", "해당 품사에 품사 상세 데이터가 있어 삭제할 수 없습니다."),
    DEFAULT_GROUP_EDIT_FAILED(3052, "기본 품사는 삭제하거나 수정할 수 없습니다.", "기본 품사는 삭제하거나 수정할 수 없습니다."),

    DAILY_SENTENCE_NOT_FOUND(4000, "해당하는 오늘의 문장 데이터를 찾을 수 없습니다.", "해당하는 오늘의 문장 데이터가 없습니다"),

    POST_NOT_OWNED(5000, "해당 게시글에 권한이 없습니다.", "게시글에 권한이 없습니다."),

    QUIZ_DATA_EXISTS(5050, "해당하는 퀴즈 데이터가 있습니다.", "해당하는 퀴즈 데이터가 있습니다."),
    ;

    private final int code;
    private final String description;
    private final String remark;
}
