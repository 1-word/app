package com.numo.wordapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SynonymRequestDto {
    private int synonym_id;
    private String synonym;
    private String memo;
    private Word word;

    public Synonym toEntity(){
        Synonym synonyms = Synonym.builder()
                .synonym_id(synonym_id)
                .synonym(synonym)
                .memo(memo)
                .word(word)
                .build();
        return synonyms;
    }
}
