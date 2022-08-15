package com.numo.wordapp.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numo.wordapp.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User extends Timestamped {
    @JsonIgnore
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String nickname;
    private String password;

    @JsonIgnore
    private boolean activated;

   @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_authority",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "authority_name"))
    private Set<Authority> authorities;

    /*@Enumerated(EnumType.STRING)
    private Role role;
    */
    public void setPassword(String password){
        this.password = password;
    }

    /*@Builder
    public User(String username, String nickname, String password, Role role){
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }*/
}
