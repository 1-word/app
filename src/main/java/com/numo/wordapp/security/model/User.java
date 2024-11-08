package com.numo.wordapp.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.numo.wordapp.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="user")
public class User extends Timestamped {
//    @JsonIgnore
//    @Id
//    @Column(name = "user_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long userId;

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;
    private String password;

    @JsonIgnore
    private boolean activated;

   @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_authority",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "authority_name"))
    private Set<Authority> authorities;

    public void setPassword(String password){
        this.password = password;
    }

    public List<String> getAuthNameList(){
        Set<Authority> authoritys = this.getAuthorities();
        List<String> list = new ArrayList<>();
        for (Authority authority : authoritys){
            list.add(authority.getAuthorityName());
        }
        return list;
    }
}
