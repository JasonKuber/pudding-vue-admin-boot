package com.pudding.repository.cache.permission.impl;

import com.pudding.repository.cache.permission.PuddingPermissionRoleCache;
import com.pudding.repository.cache.redis.SetOperations;
import com.pudding.repository.po.PuddingPermissionRolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PuddingPermissionRoleCacheImpl implements PuddingPermissionRoleCache {

    private final SetOperations<PuddingPermissionRolePO> setOperations;

    private final String PERMISSION_ROLE = "permission:role:";


    @Override
    public Set<PuddingPermissionRolePO> getCacheByRoleId(Long roleId) {
        String redisKey = PERMISSION_ROLE + roleId;
        return setOperations.sMembers(redisKey);
    }

    @Override
    public void cachePermissionRolePOList(Long roleId,List<PuddingPermissionRolePO> permissionRolePOList) {
        String redisKey = PERMISSION_ROLE + roleId;
        setOperations.sAdd(redisKey, permissionRolePOList.toArray(new PuddingPermissionRolePO[0]));

    }
}
