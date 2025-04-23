package com.numo.batch.word;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@JobScope
public class DataShare<T> {

    private Map<String, T> shareDataMap;

    public DataShare() {
        this.shareDataMap = new ConcurrentHashMap<>();
    }

    public void putData(String key, T data) {
        if (shareDataMap ==  null) {
            log.error("Map is not initialize");
            return;
        }

        shareDataMap.put(key, data);
    }

    public T getData (String key) {
        if (shareDataMap == null) {
            return null;
        }

        return shareDataMap.get(key);
    }

    public int getSize () {
        if (this.shareDataMap == null) {
            log.error("Map is not initialize");
            return 0;
        }

        return shareDataMap.size();
    }

}
