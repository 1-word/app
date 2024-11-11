package com.numo.wordapp.service.user;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.user.ChangePasswordDto;
import com.numo.wordapp.dto.user.UpdateUserDto;
import com.numo.wordapp.dto.user.UserDto;
import com.numo.wordapp.dto.user.UserRequestDto;
import com.numo.wordapp.entity.user.Authority;
import com.numo.wordapp.entity.user.Role;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signup(UserRequestDto userDto){
        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        User user = User.builder()
                .password(passwordEncoder.encode(userDto.password()))
                .email(userDto.email())
                .nickname(userDto.nickname())
                .build();

        user = userRepository.save(user);

        Authority authority = Authority.builder()
                .userId(user.getUserId())
                .name(Role.ROLE_USER)
                .build();

        user.addAuthorities(authority);

        return UserDto.of(user);
    }

    @Transactional
    public void updatePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = findUserById(userId);
        if (!checkPassword(changePasswordDto.oldPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
        user.updatePassword(changePasswordDto.newPassword());
    }

    @Transactional
    public UserDto updateUser(Long userId, UpdateUserDto userDto) {
       User user = findUserById(userId);
       user.update(userDto);
       return UserDto.of(user);
    }

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findUserByUserId(userId);
        user.withdraw();
    }

    public UserDto getUserInfo(Long userId) {
        return findByUserId(userId);
    }

    public UserDto findUserAndCheckPassword(String email, String inputPassword) {
        User user = findUserByEmail(email);
        if (!checkPassword(inputPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }
        return UserDto.of(user);
    }

    public boolean checkPassword(String inputPassword, String password) {
        return passwordEncoder.matches(inputPassword, password);
    }

    public UserDto findByUserId(Long userId) {
        User user = userRepository.findUserByUserId(userId);
        return UserDto.of(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        return UserDto.of(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findUserByUserId(userId);
    }

    private User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
