package com.pudding.manager.permission.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pudding.domain.model.entity.PuddingPermissionRoleEntity;
import com.pudding.manager.permission.PuddingPermissionRoleManager;
import com.pudding.repository.cache.permission.PuddingPermissionRoleCache;
import com.pudding.repository.po.PuddingPermissionRolePO;
import com.pudding.repository.service.PuddingPermissionRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PuddingPermissionRoleManagerImpl implements PuddingPermissionRoleManager {

    private final PuddingPermissionRoleService puddingPermissionRoleService;

    private final PuddingPermissionRoleCache puddingPermissionRoleCache;


    @Override
    public List<PuddingPermissionRoleEntity> getPermissionRoleByRoleId(Long roleId) {

        List<PuddingPermissionRoleEntity> puddingPermissionRoleEntityList = new ArrayList<>();
        Set<PuddingPermissionRolePO> permissionRolePOSet = puddingPermissionRoleCache.getCacheByRoleId(roleId);
        if (CollectionUtil.isEmpty(permissionRolePOSet)) {

            List<PuddingPermissionRolePO> permissionRolePOList = puddingPermissionRoleService.listByRoleId(roleId);
            puddingPermissionRoleCache.cachePermissionRolePOList(roleId,permissionRolePOList);

            for (PuddingPermissionRolePO po : permissionRolePOList) {
                puddingPermissionRoleEntityList.add(new PuddingPermissionRoleEntity(po.getPermId(),po.getRoleId()));
            }
        } else {
            if (CollectionUtil.isNotEmpty(permissionRolePOSet)) {
                for (PuddingPermissionRolePO po : permissionRolePOSet) {
                    puddingPermissionRoleEntityList.add(new PuddingPermissionRoleEntity(po.getPermId(),po.getRoleId()));
                }
            }
        }

        return puddingPermissionRoleEntityList;
    }
}
