package com.numo.wordapp.repository.dictionary.query;

import com.numo.wordapp.entity.dictionary.Dictionary;
import com.numo.wordapp.entity.dictionary.QDictionary;
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
