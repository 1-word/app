package com.numo.wordapp.entity.user;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="authority")
public class Authority {

    @Id
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Role name;

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
