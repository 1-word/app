package com.numo.wordapp.entity.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VerificationCode {
    String code;
    boolean verified;

    public VerificationCode(String code) {
        this.code = code;
        verified = false;
    }

    public void completeVerification() {
        this.verified = true;
    }

    public boolean checkVerificationCode(String code) {
        return Objects.equals(this.code, code);
    }
}
