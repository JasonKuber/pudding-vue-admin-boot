package com.pudding.repository.cache.permission;

import com.pudding.repository.po.PuddingApiPermissionPO;

import java.util.List;

public interface PuddingApiPermissionCache {
    List<PuddingApiPermissionPO> getCacheAllPermission();

    void cacheAllPermission(List<PuddingApiPermissionPO> apiPermissionPOList);

    List<PuddingApiPermissionPO> getCachePermissionByIdList(List<Long> permIdList);

    void cacheApiPermission(PuddingApiPermissionPO puddingApiPermissionPO);

    void updateCacheApiPermission(PuddingApiPermissionPO apiPermissionPO);

    void deleteCacheApiPermission(Long apiPermissionId);
}
