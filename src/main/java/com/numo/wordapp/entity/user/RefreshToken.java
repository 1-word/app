package com.numo.wordapp.entity.user;

import com.numo.wordapp.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
public class RefreshToken extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false)
    private String token;

    public RefreshToken updateToken(String token){
        this.token = token;
        return this;
    }

    @Builder
    public RefreshToken(String user_id, String token){
        this.userId = user_id;
        this.token = token;
    }
}
