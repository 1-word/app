package com.numo.wordapp.service.dictionary;

import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class DictionaryCacheService {
    private final ZSetOperations<String, String> zSetOperations;

    public DictionaryCacheService(StringRedisTemplate redisTemplate) {
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public List<String> search(String key, String searchText, int limit) {
        Range<String> range = Range.closed(searchText, getEnd(searchText));
        Limit count = Limit.limit().count(limit);

        LinkedHashSet<String> result = (LinkedHashSet<String>) zSetOperations.rangeByLex(key, range, count);
        return List.copyOf(result);
    }

    public String save(String key, String value) {
        zSetOperations.add(key, value, 0);
        return value;
    }

    private String getEnd(String searchText) {
        return searchText + (char) 0xFF;
    }
}
