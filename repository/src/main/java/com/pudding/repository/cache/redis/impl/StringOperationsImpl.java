package com.pudding.repository.cache.redis.impl;

import com.pudding.repository.cache.redis.StringOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StringOperationsImpl<V> implements StringOperations<V> {

    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public void set(String key, V value) {
        log.debug("set(...) -> key -> {}, value: {}",key,value);
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void set(String key, V value, long timeout, TimeUnit timeUnit) {
        log.debug("set(...) => key -> {}, value -> {}, timeout ->{}, timeunit -> {}",key,value,timeout,timeUnit);
        redisTemplate.opsForValue().set(key,value,timeout,timeUnit);
    }

    @Override
    public V get(String key) {
        log.debug("get(...) => key -> {}",key);
        return (V) redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean setIfAbsent(String key, V value) {
        log.debug("setIfAbsent(...) => key -> {}, value -> {}",key,value);
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public boolean setIfAbsent(String key, V value, long timeout, TimeUnit unit) {
        log.debug("setIfAbsent(...) => key -> {}, value -> {}, timeout -> {}, unit -> {}",key,value,timeout,unit);
        return redisTemplate.opsForValue().setIfAbsent(key,value,timeout,unit);
    }

    @Override
    public long incrBy(String key, long increment) {
        log.debug("incrBy(...) => key -> {}, increment -> {}",key,increment);
        Long incremented = redisTemplate.opsForValue().increment(key, increment);
        if (incremented == null) {
            throw new NullPointerException();
        }
        return incremented;
    }

    @Override
    public double incrByFloat(String key, double increment) {
        log.debug("incrByFloat(...) => key -> {}, increment -> {}",key,increment);
        Double incremented = redisTemplate.opsForValue().increment(key, increment);
        if (incremented == null) {
            throw new NullPointerException();
        }
        return incremented;
    }
}
