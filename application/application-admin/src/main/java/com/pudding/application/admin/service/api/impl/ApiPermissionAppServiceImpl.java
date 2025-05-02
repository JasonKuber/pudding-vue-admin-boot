package com.pudding.application.admin.service.api.impl;

import com.pudding.application.admin.service.api.ApiPermissionAppService;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.domain.model.convert.PuddingApiPermissionEntityConvert;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiPermissionAppServiceImpl implements ApiPermissionAppService {

    private final PuddingApiPermissionManager apiPermissionManager;

    @Override
    public void addApiPermission(AdminLoginUser loginUser,
                                 AddApiPermissionDTO addApiPermissionDTO) {
        apiPermissionManager.addApiPermission(loginUser,PuddingApiPermissionEntityConvert.toEntity(addApiPermissionDTO));

    }
}
