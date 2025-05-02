package com.pudding.repository.cache.permission.impl;

import com.pudding.repository.cache.permission.PuddingApiPermissionCache;
import com.pudding.repository.cache.redis.HashOperations;
import com.pudding.repository.po.PuddingApiPermissionPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuddingApiPermissionCacheImpl implements PuddingApiPermissionCache {

    private final String ALL_PERMISSION = "permission:api:all";

    private final HashOperations<String, PuddingApiPermissionPO> hashOperations;


    @Override
    public List<PuddingApiPermissionPO> getCacheAllPermission() {
        return hashOperations.hValues(ALL_PERMISSION);
    }

    @Override
    public void cacheAllPermission(List<PuddingApiPermissionPO> apiPermissionPOList) {
        Map<String, PuddingApiPermissionPO> permissionPOMap = apiPermissionPOList
                .stream()
                .collect(Collectors.toMap(po -> String.valueOf(po.getId()), po -> po));
        hashOperations.hPutAll(ALL_PERMISSION,permissionPOMap);
    }

    @Override
    public List<PuddingApiPermissionPO> getCachePermissionByIdList(List<Long> permIdList) {
        return hashOperations.hMultiGet(ALL_PERMISSION,permIdList.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    @Override
    public void cacheApiPermission(PuddingApiPermissionPO puddingApiPermissionPO) {
        hashOperations.hPutIfAbsent(ALL_PERMISSION,puddingApiPermissionPO.getId().toString(),puddingApiPermissionPO);
    }
}
