package com.pudding.manager.permission.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.manager.convert.PuddingApiPermissionConvert;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import com.pudding.repository.cache.permission.PuddingApiPermissionCache;
import com.pudding.repository.po.PuddingApiPermissionPO;
import com.pudding.repository.service.PuddingApiPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PuddingApiPermissionManagerImpl implements PuddingApiPermissionManager {



    private final PuddingApiPermissionService puddingApiPermissionService;

    private final PuddingApiPermissionCache puddingApiPermissionCache;


    @Override
    public List<PuddingApiPermissionEntity> listPermissionByIdList(List<Long> permIdList) {

        List<PuddingApiPermissionPO> apiPermissionPOList = puddingApiPermissionCache.getCachePermissionByIdList(permIdList);
        if (CollectionUtil.isEmpty(apiPermissionPOList)) {


            List<PuddingApiPermissionEntity> newPermissionEntityList = new ArrayList<>();
            // 初始化一下Permission列表
            List<PuddingApiPermissionEntity> permissionEntityList = allPermission();
            for (PuddingApiPermissionEntity entity : permissionEntityList) {
                if (permIdList.contains(entity.getId())) {
                    newPermissionEntityList.add(entity);
                }
            }
            return newPermissionEntityList;
        }

        return PuddingApiPermissionConvert.toEntityList(apiPermissionPOList);

    }

    @Override
    public List<PuddingApiPermissionEntity> allPermission() {

        List<PuddingApiPermissionPO> apiPermissionPOList = puddingApiPermissionCache.getCacheAllPermission();
        if (CollectionUtil.isEmpty(apiPermissionPOList)) {
            apiPermissionPOList = puddingApiPermissionService.listPermission();
            puddingApiPermissionCache.cacheAllPermission(apiPermissionPOList);
        }
        return PuddingApiPermissionConvert.toEntityList(apiPermissionPOList);
    }
}
