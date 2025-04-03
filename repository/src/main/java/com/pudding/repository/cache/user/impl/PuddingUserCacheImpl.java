package com.pudding.repository.cache.user.impl;

import com.pudding.repository.cache.redis.StringOperations;
import com.pudding.repository.cache.user.PuddingUserCache;
import com.pudding.repository.po.PuddingUserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuddingUserCacheImpl implements PuddingUserCache {

    private final StringOperations<PuddingUserPO> stringOperations;

}
