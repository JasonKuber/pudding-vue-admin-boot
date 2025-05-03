package com.pudding.manager.permission;

import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.vo.PageResult;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;

import java.util.List;

public interface PuddingApiPermissionManager {
    List<PuddingApiPermissionEntity> listPermissionByIdList(List<Long> permIdList);

    List<PuddingApiPermissionEntity> allPermission();

    void addApiPermission(AdminLoginUser loginUser, AddApiPermissionDTO addApiPermissionDTO);

    PageResult<PuddingApiPermissionEntity> pageApiPermissionList(PageApiPermissionListDTO entity);
}
