package com.pudding.repository.cache.permission;

import com.pudding.repository.po.PuddingPermissionRolePO;

import java.util.List;
import java.util.Set;

public interface PuddingPermissionRoleCache {
    Set<PuddingPermissionRolePO> getCacheByRoleId(Long roleId);

    void cachePermissionRolePOList(Long roleId,List<PuddingPermissionRolePO> permissionRolePOList);

}
