package com.numo.api.global.comm.redis;

import com.numo.api.global.comm.util.JsonUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    public void save(String key, Object value, Duration duration) {
        String result = JsonUtil.toJson(value);
        save(key, result, duration);
    }

    public void save(String key, String value) {
        valueOperations.set(key, value);
    }

    public void save(String key, String value, Duration duration) {
        valueOperations.set(key, value, duration);
    }

    public <T> Optional<T> get(String key, Class<T> classType) {
        String value = valueOperations.get(key);
        if (StringUtils.hasText(value)) {
            return Optional.ofNullable(JsonUtil.makeObject(value, classType));
        }
        return Optional.empty();
    }

    public void delete(String key) {
        valueOperations.getAndDelete(key);
    }
}
