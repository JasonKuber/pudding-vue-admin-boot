package com.pudding.repository.cache.user;

import com.pudding.repository.po.PuddingUserPO;

public interface PuddingUserCache {


    void cacheById(String id, PuddingUserPO puddingUserPO);

    PuddingUserPO getCacheById(String id);

}
