package com.pudding.application.admin.service.api;

import com.pudding.common.security.AdminLoginUser;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;

public interface ApiPermissionAppService {
    void addApiPermission(AdminLoginUser loginUser, AddApiPermissionDTO addApiPermissionDTO);
}
