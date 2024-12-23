package com.numo.wordapp.entity.word.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WordGroupType {

    noun("명사"),
    pronoun("대명사"),
    verb("동사"),
    adjective("형용사"),
    adverb("부사"),
    conjunction("접속사"),
    preposition("전치사"),
    interjection("감탄사"),
    ;
    final String name;

}
