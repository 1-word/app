package com.numo.wordapp.entity.user;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="roles")
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String user_id;
    @Enumerated(EnumType.STRING)
    private Role name;
     */
}
