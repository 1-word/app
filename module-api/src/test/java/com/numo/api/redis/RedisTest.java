package com.numo.api.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Transactional
    @Test
    void sortedSet() {
        String key = "dict";
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();

        // 저장
        zSet.add(key, "ab123", 0);

        String search = "a";
        // 검색은 "[a\xff"로 되어야하는데, 자바에서는 \를 인식하지 못하는 문제로 "[a\\xff"로 전달되는 문제가 있어 0xFF를 변환
        String end = search + (char) 0xFF;

        // 검색
        Object set = zSet.rangeByLex(key, Range.closed(search, end), Limit.limit().count(20));

        System.out.println(set);
    }
}
