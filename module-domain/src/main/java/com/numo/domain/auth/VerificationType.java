package com.numo.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VerificationType {
    signup("회원가입"),
    pw("비밀번호 찾기"),
    ;

    final String name;
}
