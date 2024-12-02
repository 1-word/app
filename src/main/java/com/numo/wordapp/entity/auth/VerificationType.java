package com.numo.wordapp.entity.auth;

import com.numo.wordapp.comm.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VerificationType {
    signup("회원가입", ErrorCode.DUPLICATED_ID),
    pw("비밀번호 찾기", ErrorCode.DATA_NOT_FOUND),
    ;

    final String name;
    final ErrorCode errorCode;
}
