package com.pudding.application.admin.service.api.impl;

import com.pudding.application.admin.service.api.ApiPermissionAppService;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.vo.PageResult;
import com.pudding.domain.model.convert.PuddingApiPermissionEntityConvert;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.dto.api.UpdateApiPermissionDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.domain.model.vo.api.PageApiPermissionListVO;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiPermissionAppServiceImpl implements ApiPermissionAppService {

    private final PuddingApiPermissionManager apiPermissionManager;

    @Override
    public void addApiPermission(AdminLoginUser loginUser,
                                 AddApiPermissionDTO addApiPermissionDTO) {
        apiPermissionManager.addApiPermission(loginUser,addApiPermissionDTO);

    }

    @Override
    public PageResult<PageApiPermissionListVO> pageApiPermissionList(PageApiPermissionListDTO pageApiPermissionListDTO) {
        PageResult<PuddingApiPermissionEntity> pageResultEntity = apiPermissionManager.pageApiPermissionList(pageApiPermissionListDTO);
        List<PageApiPermissionListVO> pageApiPermissionListVOList = PuddingApiPermissionEntityConvert.toVOList(pageResultEntity.getRecordList());
        return PageResult.of(pageApiPermissionListVOList,pageResultEntity.getTotal(),pageResultEntity.getCurrent(),pageResultEntity.getSize());
    }

    @Override
    public void updateApiPermission(AdminLoginUser loginUser,
                                    Long apiPermissionId,
                                    UpdateApiPermissionDTO updateApiPermissionDTO) {

        apiPermissionManager.updateApiPermission(loginUser,apiPermissionId,updateApiPermissionDTO);

    }

    @Override
    public void deleteApiPermission(AdminLoginUser loginUser,Long apiPermissionId) {
        apiPermissionManager.deleteApiPermission(loginUser,apiPermissionId);
    }
}
