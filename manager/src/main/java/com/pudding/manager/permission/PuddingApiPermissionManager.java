package com.pudding.manager.permission;

import com.pudding.domain.model.entity.PuddingApiPermissionEntity;

import java.util.List;

public interface PuddingApiPermissionManager {
    List<PuddingApiPermissionEntity> listPermissionByIdList(List<Long> permIdList);

    List<PuddingApiPermissionEntity> allPermission();

}
