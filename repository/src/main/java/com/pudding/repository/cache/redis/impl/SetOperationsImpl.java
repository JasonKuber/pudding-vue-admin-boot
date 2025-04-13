package com.pudding.repository.cache.redis.impl;

import com.pudding.repository.cache.redis.SetOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetOperationsImpl<V> implements SetOperations<V> {

    private final RedisTemplate<String,Object> redisTemplate;


    @Override
    public Long sAdd(String key, V... items) {
        log.debug("sAdd(...) => key -> {}, items -> {}", key, items);
        Long count = redisTemplate.opsForSet().add(key, items);
        log.debug("sAdd(...) => count -> {}", count);
        if (count == null) {
            throw new NullPointerException();
        }
        return count;
    }

    @Override
    public Long sRemove(String key, V... items) {
        log.debug("sRemove(...) => key -> {}, items -> {}", key, items);
        Long count = redisTemplate.opsForSet().remove(key, items);
        log.debug("sRemove(...) => count -> {}", count);
        if (count == null) {
            throw new NullPointerException();
        }
        return count;
    }

    @Override
    public Long sSize(String key) {
        log.debug("sSize(...) => key -> {}", key);
        Long size = redisTemplate.opsForSet().size(key);
        log.debug("sSize(...) => size -> {}", size);
        if (size == null) {
            throw new NullPointerException();
        }
        return size;
    }

    @Override
    public Boolean sIsMember(String key, V item) {
        log.debug("sSize(...) => key -> {}, size -> {}", key, item);
        Boolean result = redisTemplate.opsForSet().isMember(key, item);
        log.debug("sSize(...) => result -> {}", result);
        if (result == null) {
            throw new NullPointerException();
        }
        return result;
    }

    @Override
    public Set<V> sMembers(String key) {
        log.debug("sMembers(...) => key -> {}", key);
        Set<V> members = (Set<V>) redisTemplate.opsForSet().members(key);
        log.debug("sMembers(...) => members -> {}", members);
        return members;
    }
}
