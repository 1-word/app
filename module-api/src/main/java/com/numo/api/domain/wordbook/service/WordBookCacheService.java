package com.numo.api.domain.wordbook.service;

import com.numo.api.domain.wordbook.repository.WordBookRepository;
import com.numo.domain.wordbook.WordBook;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordBookCacheService {
    private final WordBookRepository wordBookRepository;

    @Cacheable(cacheNames = "wordBook", key = "#p0")
    public WordBook findWordBook(Long wordBookId) {
        return wordBookRepository.findWordBookById(wordBookId);
    }

    public WordBook findWordBookNoCache(Long wordBookId) {
        return wordBookRepository.findWordBookById(wordBookId);
    }
}
