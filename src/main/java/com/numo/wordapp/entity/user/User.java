package com.numo.wordapp.entity.user;

import com.numo.wordapp.comm.exception.CustomException;
import com.numo.wordapp.comm.exception.ErrorCode;
import com.numo.wordapp.dto.user.UpdateUserDto;
import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.file.File;
import com.numo.wordapp.security.oauth2.info.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String email;
    private String nickname;
    private String password;
    private String profileImagePath;
    private LocalDateTime withdrawDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId", updatable = false)
    private Set<Authority> authorities;

    private String serviceType;

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public List<String> getAuthNameList(){
        List<String> list = new ArrayList<>();
        if (authorities.isEmpty()) {
            return list;
        }
        Set<Authority> authorities = this.getAuthorities();
        for (Authority authority : authorities){
            list.add(authority.getName().name());
        }
        return list;
    }

    public void checkUser() {
        if (this.withdrawDate != null) {
            throw new CustomException(ErrorCode.WITHDRAWN_ACCOUNT);
        }
    }

    public void update(UpdateUserDto userDto) {
        this.nickname = userDto.nickname();
        this.profileImagePath = userDto.profileImagePath();
    }

    public void withdraw() {
        this.withdrawDate = LocalDateTime.now();
    }

    public void addAuthorities(Authority authority) {
        if (this.authorities == null) {
            authorities = new HashSet<>();
        }
        this.authorities.add(authority);
        authority.setUserId(userId);
    }

    public User update(OAuth2UserInfo userInfo) {
        this.profileImagePath = userInfo.thumbnail();
        if (!Objects.equals(this.serviceType, userInfo.clientName())) {
            throw new CustomException(ErrorCode.OAUTH2_EMAIL_EXISTS);
        }
//        this.thumbnail = userInfo.thumbnail();
        this.nickname = userInfo.nickname();
        return this;
    }

    private File getThumbnail(String fileId) {
        return File.builder()
                .fileId(fileId)
                .build();
    }

}
