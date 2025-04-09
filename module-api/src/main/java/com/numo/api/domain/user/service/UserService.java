package com.numo.api.domain.user.service;

import com.numo.api.domain.user.dto.SearchUserDto;
import com.numo.api.domain.user.repository.query.UserQueryRepository;
import com.numo.domain.auth.VerificationCode;
import com.numo.domain.user.Authority;
import com.numo.domain.user.Role;
import com.numo.domain.user.User;
import com.numo.domain.user.dto.UpdateUserDto;
import com.numo.api.global.comm.exception.CustomException;
import com.numo.api.global.comm.exception.ErrorCode;
import com.numo.api.global.comm.redis.RedisService;
import com.numo.api.domain.user.dto.ChangePasswordDto;
import com.numo.api.domain.user.dto.UserDto;
import com.numo.api.domain.user.dto.UserRequestDto;
import com.numo.api.domain.user.repository.UserRepository;
import com.numo.api.security.oauth2.info.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserCacheService userCacheService;
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    /**
     * 검증된 메일만 회원가입
     * @param userDto 회원가입할 유저 데이터
     * @return 회원가입된 유저 데이터
     */
    @Transactional
    public UserDto signup(UserRequestDto userDto){
        VerificationCode verificationCode = redisService.get(userDto.email(), VerificationCode.class).orElseThrow(
                () -> new CustomException(ErrorCode.SIGNUP_FAILED)
        );

        if (!verificationCode.isVerified()) {
            throw new CustomException(ErrorCode.UNVERIFIED_EMAIL);
        }

        redisService.delete(userDto.email());

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.password()))
                .email(userDto.email())
                .nickname(userDto.nickname())
                // 썸네일 추가
                .profileImagePath(userDto.profileImagePath())
                .build();

        user = userRepository.save(user);

        Authority authority = Authority.builder()
                .userId(user.getUserId())
                .name(Role.ROLE_USER)
                .build();

        user.addAuthorities(authority);

        return UserDto.of(user);
    }

    /**
     * 로그인 유저의 비밀번호를 변경한다
     * @param userId 유저 아이디
     * @param changePasswordDto 변경할 비밀번호 데이터
     */
    @Transactional
    public void updatePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = getUserById(userId);

        if (!checkPassword(changePasswordDto.oldPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        updatePassword(user, changePasswordDto);
    }

    /**
     * 비로그인 유저의 비밀번호를 변경한다
     * @param changePasswordDto 변경할 데이터
     */
    @Transactional
    public void updatePassword(ChangePasswordDto changePasswordDto) {
        String email = changePasswordDto.email();

        verifiedUpdatePasswordCode(email);
        User user = userCacheService.getUserByEmail(email);

        updatePassword(user, changePasswordDto);
    }

    /**
     * 비로그인 유저의 비밀번호 변경 시 인증 여부 확인.
     * @param email key 값
     */
    private void verifiedUpdatePasswordCode(String email) {
        VerificationCode verificationCode = redisService.get(email, VerificationCode.class).orElseThrow(
                () -> new CustomException(ErrorCode.CHANGE_PASSWORD_FAILED)
        );

        if (!verificationCode.isVerified()) {
            throw new CustomException(ErrorCode.UNVERIFIED_EMAIL);
        }

        redisService.delete(email);
    }

    /**
     * 비밀번호를 변경한다
     * @param user 변경할 유저 데이터
     * @param changePasswordDto 변경할 비밀번호 데이터
     */
    private void updatePassword(User user, ChangePasswordDto changePasswordDto) {
        String newPassword = passwordEncoder.encode(changePasswordDto.newPassword());
        user.updatePassword(newPassword);
    }

    @CacheEvict(cacheNames = "user", key = "#p0")
    public UserDto updateUser(Long userId, UpdateUserDto userDto) {
       User user = getUserById(userId);
       user.update(userDto);
       return UserDto.of(userRepository.save(user));
    }

    @Transactional
    @CacheEvict(cacheNames = "user", key = "#p0")
    public void withdraw(Long userId) {
        User user = userRepository.findUserByUserId(userId);
        user.withdraw();
    }

    public UserDto getUserInfo(Long userId) {
        return findByUserId(userId);
    }

    public UserDto findUserAndCheckPassword(String email, String inputPassword) {
        User user = userCacheService.getUserByEmail(email);
        if (!checkPassword(inputPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_PW_FAILED);
        }
        if (!user.isActivatedUser()) {
            throw new CustomException(ErrorCode.WITHDRAWN_ACCOUNT);
        }
        return UserDto.of(user);
    }

    public UserDto findByUserId(Long userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserDto.of(user);
    }

    @Transactional
    public UserDto saveOrUpdate(OAuth2UserInfo userInfo) {
        if (userInfo.email() == null) {
            throw new CustomException(ErrorCode.OAUTH2_EMAIL_NULL);
        }

        User user = userRepository.findByEmail(userInfo.email())
                .map(u -> checkAndUpdateUser(userInfo, u))
                .orElse(userInfo.toEntity());

        user = userRepository.save(user);

        if (user.getAuthorities() == null) {
            Authority authority = Authority.builder()
                    .userId(user.getUserId())
                    .name(Role.ROLE_USER)
                    .build();

            user.addAuthorities(authority);
        }

        return UserDto.of(user);
    }

    public List<SearchUserDto> searchUsers(String searchText) {
        return userQueryRepository.searchUsers(searchText, 5);
    }

    private User checkAndUpdateUser(OAuth2UserInfo userInfo, User u) {
        if (!Objects.equals(u.getServiceType(), userInfo.clientName())) {
            throw new CustomException(ErrorCode.OAUTH2_EMAIL_EXISTS);
        }
        return u.update(u.getNickname(), u.getProfileImagePath());
    }

    private User getUserById(Long userId) {
        return userRepository.findUserByUserId(userId);
    }

    private boolean checkPassword(String inputPassword, String password) {
        return passwordEncoder.matches(inputPassword, password);
    }
}
