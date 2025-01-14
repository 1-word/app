package com.numo.api.domain.dictionary.repository.query;

import com.numo.api.domain.dictionary.dto.DictionaryDto;
import com.numo.domain.dictionary.Dictionary;
import com.numo.domain.dictionary.QDictionary;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DictionaryCustomRepositoryImpl implements DictionaryCustomRepository {
    private final JPAQueryFactory queryFactory;
    QDictionary dictionary = QDictionary.dictionary;

    @Override
    public List<Dictionary> findByWordWithLimit(String word, int limit) {
        List<Dictionary> result = queryFactory.selectDistinct(dictionary)
                .from(dictionary)
                .where(dictionary.word.startsWith(word))
                .limit(limit)
                .fetch();
        return result;
    }

    @Override
    public List<DictionaryDto> findDictList(String word) {
        List<DictionaryDto> result = queryFactory.select(Projections.constructor(
                        DictionaryDto.class,
                        dictionary.dictId,
                        dictionary.word,
                        dictionary.wordType,
                        dictionary.definition,
                        dictionary.mean
                ))
                .from(dictionary)
                .where(dictionary.word.startsWith(word))
                .limit(5)
                .fetch();
        return result;
    }
}
