package com.pudding.application.admin.service.api;

import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.vo.PageResult;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.dto.api.UpdateApiPermissionDTO;
import com.pudding.domain.model.vo.api.PageApiPermissionListVO;

public interface ApiPermissionAppService {
    void addApiPermission(AdminLoginUser loginUser, AddApiPermissionDTO addApiPermissionDTO);

    PageResult<PageApiPermissionListVO> pageApiPermissionList(PageApiPermissionListDTO pageApiPermissionListDTO);

    void updateApiPermission(AdminLoginUser loginUser, Long apiPermissionId, UpdateApiPermissionDTO updateApiPermissionDTO);

    void deleteApiPermission(AdminLoginUser loginUser,Long apiPermissionId);
}
