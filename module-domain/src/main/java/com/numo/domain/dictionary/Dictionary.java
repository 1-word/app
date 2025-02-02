package com.numo.domain.dictionary;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dictId;
    private String word;
    private String wordType;
    @Column(columnDefinition="TEXT")
    private String definition;
    @Column(columnDefinition="TEXT")
    private String mean;
    private String isCrawling;

    public boolean checkCrawling() {
        return Objects.isNull(isCrawling);
    }

    public void updateMean(String mean) {
        this.mean = mean;
        this.isCrawling = "Y";
    }
}
