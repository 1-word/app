package com.numo.domain.user;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.dto.UpdateUserDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
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
    private boolean isOnboardingFinished;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "userId", updatable = false)
    private Set<Authority> authorities;

    private String serviceType;

    public User(Long userId,
                String email,
                String nickname,
                String password,
                String profileImagePath,
                LocalDateTime withdrawDate,
                boolean isOnboardingFinished,
                Set<Authority> authorities,
                String serviceType) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImagePath = profileImagePath;
        this.withdrawDate = withdrawDate;
        this.isOnboardingFinished = false;
        this.authorities = authorities;
        this.serviceType = serviceType;
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    public List<String> getAuthNameList(){
        if (authorities == null) {
            return null;
        }
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

    public void completeOnboarding() {
        this.isOnboardingFinished = true;
    }

    public boolean isActivatedUser() {
        return this.withdrawDate == null;
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

    public User update(String nickname, String profileImagePath) {
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        return this;
    }

}
