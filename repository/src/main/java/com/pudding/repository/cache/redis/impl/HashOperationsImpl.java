package com.pudding.repository.cache.redis.impl;

import com.pudding.repository.cache.redis.HashOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashOperationsImpl<HK,HV> implements HashOperations<HK,HV> {

    private final RedisTemplate<String,Object> redisTemplate;


    @Override
    public void hPut(String key, HK entryKey, HV entryValue) {
        log.debug("hPut(...) => key -> {}, entryKey -> {}, entryValue -> {}", key, entryKey, entryValue);
        redisTemplate.opsForHash().put(key, entryKey, entryValue);
    }

    @Override
    public void hPutAll(String key, Map<HK, HV> maps) {
        log.debug("hPutAll(...) => key -> {}, maps -> {}", key, maps);
        redisTemplate.opsForHash().putAll(key, maps);
    }

    @Override
    public HV hGet(String key, HK entryKey) {
        log.debug("hGet(...) => key -> {}, entryKey -> {}", key, entryKey);
        HV entryValue = (HV) redisTemplate.opsForHash().get(key, entryKey);
        log.debug("hGet(...) => entryValue -> {}", entryValue);
        return entryValue;
    }

    @Override
    public Map<HK, HV> hGetAll(String key) {
        log.debug("hGetAll(...) => key -> {}",  key);
        Map<HK, HV> result = (Map<HK, HV>) redisTemplate.opsForHash().entries(key);
        log.debug("hGetAll(...) => result -> {}", result);
        return result;
    }

    @Override
    public List<HV> hMultiGet(String key, Collection<HK> entryKeys) {
        log.debug("hMultiGet(...) => key -> {}, entryKeys -> {}", key, entryKeys);
        List<HV> entryValues = (List<HV>) redisTemplate.opsForHash().multiGet(key, (Collection<Object>) entryKeys);
        log.debug("hMultiGet(...) => entryValues -> {}", entryValues);
        return entryValues;
    }

    @Override
    public Long hDelete(String key, HK... entryKeys) {
        log.debug("hDelete(...) => key -> {}, entryKeys -> {}", key, entryKeys);
        Long count = redisTemplate.opsForHash().delete(key, entryKeys);
        log.debug("hDelete(...) => count -> {}", count);
        return count;
    }

    @Override
    public Boolean hExists(String key, HK entryKey) {
        log.debug("hDelete(...) => key -> {}, entryKeys -> {}", key, entryKey);
        Boolean exist = redisTemplate.opsForHash().hasKey(key, entryKey);
        log.debug("hDelete(...) => exist -> {}", exist);
        return exist;
    }

    @Override
    public Set<HK> hKeys(String key) {
        log.debug("hKeys(...) => key -> {}", key);
        Set<HK> entryKeys = (Set<HK>) redisTemplate.opsForHash().keys(key);
        log.debug("hKeys(...) => entryKeys -> {}", entryKeys);
        return entryKeys;
    }

    @Override
    public List<HV> hValues(String key) {
        log.debug("hValues(...) => key -> {}", key);
        List<HV> entryValues = (List<HV>) redisTemplate.opsForHash().values(key);
        log.debug("hValues(...) => entryValues -> {}", entryValues);
        return entryValues;
    }
}
