package com.pudding.repository.cache.redis.impl;

import com.pudding.repository.cache.redis.KeyValueOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyValueOperationsImpl implements KeyValueOperations {

    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public Boolean delete(String key) {
        log.debug("delete(...) => key -> {}",key);
        return redisTemplate.delete(key);
    }

    @Override
    public Boolean hasKey(String key) {
        log.debug("hasKey(...) => key -> {}",key);
        return redisTemplate.hasKey(key);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        log.debug("expire(...) => key -> {}, timeout -> {}, unit -> {}",key,timeout,unit);
        redisTemplate.expire(key,timeout,unit);
    }

    @Override
    public void expireAt(String key, Date date) {
        log.debug("expireAt(...) => key -> {}, date -> {}",key,date);
        redisTemplate.expireAt(key,date);
    }

    @Override
    public long getExpire(String key, TimeUnit unit) {
        log.debug("getExpire(...) => key -> {}, unit -> {}",key,unit);
        Long expire = redisTemplate.getExpire(key, unit);
        if (expire == null) {
            throw new NullPointerException();
        }
        return expire;
    }
}
