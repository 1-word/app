package com.numo.api.domain.dictionary.repository.query;

import com.numo.domain.dictionary.Dictionary;
import com.numo.domain.dictionary.QDictionary;
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
}
