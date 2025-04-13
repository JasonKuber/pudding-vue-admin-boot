package com.pudding.manager.permission;

import com.pudding.domain.model.entity.PuddingPermissionRoleEntity;

import java.util.List;

public interface PuddingPermissionRoleManager {
    List<PuddingPermissionRoleEntity> getPermissionRoleByRoleId(Long roleId);
}
