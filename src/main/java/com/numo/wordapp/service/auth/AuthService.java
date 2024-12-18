package com.numo.wordapp.service.auth;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.comm.mail.MailService;
import com.numo.wordapp.comm.mail.template.AuthMailTemplate;
import com.numo.wordapp.comm.mail.template.MailTemplate;
import com.numo.wordapp.comm.redis.RedisService;
import com.numo.wordapp.dto.auth.EmailRequestDto;
import com.numo.wordapp.dto.auth.LoginDto;
import com.numo.wordapp.dto.user.TokenDto;
import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.dto.user.VerificationRequestDto;
import com.numo.wordapp.entity.auth.VerificationCode;
import com.numo.wordapp.entity.auth.VerificationType;
import com.numo.wordapp.entity.user.RefreshToken;
import com.numo.wordapp.security.jwt.TokenProvider;
import com.numo.wordapp.service.user.RefreshTokenService;
import com.numo.wordapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RedisService redisService;
    private final MailService mailService;

    /**
     * 인증코드 검증
     * @param requestDto 검증 데이터
     * @return 검증 완료 메시지
     */
    public String verificationCode(VerificationRequestDto requestDto) {
        String email = requestDto.email();
        String code = requestDto.code();

        VerificationCode verificationCode = redisService.get(email, VerificationCode.class).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE)
        );

        if (!verificationCode.checkVerificationCode(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        if (verificationCode.isVerified()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFIED_ACCOUNT);
        }

        verificationCode.completeVerification();

        redisService.save(email, verificationCode, Duration.ofHours(1));

        return "인증이 완료되었습니다.";
    }

    /**
     * 메일로 인증 코드를 발송한다.
     * @param emailDto 유저 이메일
     * @return 메일 전송 완료 메시지
     */
    public String sendEmailVerificationCode(VerificationType type, EmailRequestDto emailDto) {

        boolean check = userService.existsByEmail(emailDto.email());
        if (type == VerificationType.pw) {
            // 비밀번호 변경의 경우 존재하는 이메일이 있어야 한다.
            check = !check;
        }

        if (check) {
            throw new CustomException(type.getErrorCode());
        }

        String email = emailDto.email();
        String number = String.valueOf(generateVerificationCode());
        VerificationCode verificationCode = new VerificationCode(number);
        redisService.save(email, verificationCode, Duration.ofMinutes(5));

        MailTemplate mailTemplate = new AuthMailTemplate(email, number, type.getName() );
        mailService.send(mailTemplate.createMail());
        return "해당하는 메일로 인증 메일 발송이 완료되었습니다";
    }

    /**
     * 인증코드 6자리를 랜덤으로 생성한다
     * @return 생성한 랜덤 인증코드
     */
    private int generateVerificationCode() {
        Random random = new Random();
        return (int)(Math.random() * 899999) + 100000;
    }

    public TokenDto login(LoginDto loginDto) {
        UserDto user = userService.findUserAndCheckPassword(loginDto.email(), loginDto.password());
        // 토큰 발급
        TokenDto tokenDto = tokenProvider.createTokenDto(user.email(), user.authorities());
        refreshTokenService.saveOrUpdateToken(user.userId(), tokenDto.refreshToken());
        return tokenDto;
    }

    public TokenDto reissue(String refreshToken) {
        // 만료된 refresh token 에러
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }

        // refreshToken 검색
        RefreshToken oldRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);

        // 리프레시 토큰 불일치 에러
        if (!oldRefreshToken.getToken().equals(refreshToken))
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);

        UserDto user = userService.findByUserId(oldRefreshToken.getUserId());
        TokenDto tokenDto = tokenProvider.createTokenDto(user.email(), user.authorities());
        refreshTokenService.saveOrUpdateToken(user.userId(), tokenDto.refreshToken());

        return tokenDto;
    }

    public void logout(Long userId) {
        refreshTokenService.deleteToken(userId);
    }
}
