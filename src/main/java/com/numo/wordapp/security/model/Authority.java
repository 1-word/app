package com.numo.wordapp.security.model;

import lombok.*;

import javax.persistence.*;

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
