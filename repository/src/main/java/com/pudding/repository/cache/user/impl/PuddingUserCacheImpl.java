package com.pudding.repository.cache.user.impl;

import com.pudding.repository.cache.redis.StringOperations;
import com.pudding.repository.cache.user.PuddingUserCache;
import com.pudding.repository.po.PuddingUserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PuddingUserCacheImpl implements PuddingUserCache {

    private final StringOperations<PuddingUserPO> stringOperations;


    @Override
    public void cacheById(String id, PuddingUserPO puddingUserPO) {
        String redisKey = "user:id:key:" + id;
        stringOperations.set(redisKey,puddingUserPO,1, TimeUnit.DAYS);
    }

    @Override
    public PuddingUserPO getCacheById(String id) {
        String redisKey = "user:id:key:" + id;
        return stringOperations.get(redisKey);

    }
}
