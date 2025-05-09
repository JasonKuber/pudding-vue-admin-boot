package com.pudding.manager.convert;

import cn.hutool.core.util.StrUtil;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.dto.api.UpdateApiPermissionDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.repository.po.PuddingApiPermissionPO;

import java.util.ArrayList;
import java.util.List;

public class PuddingApiPermissionConvert {
    public static List<PuddingApiPermissionEntity> toEntityList(List<PuddingApiPermissionPO> apiPermissionPOList) {
        List<PuddingApiPermissionEntity> apiPermissionEntityList = new ArrayList<>();
        for (PuddingApiPermissionPO po : apiPermissionPOList) {
            PuddingApiPermissionEntity entity = new PuddingApiPermissionEntity();
            entity.setId(po.getId());
            entity.setPermName(po.getPermName());
            entity.setPermCode(po.getPermCode());
            entity.setDescription(po.getDescription());
            entity.setPermApi(po.getPermApi());
            entity.setMethod(po.getMethod());
            entity.setCreateId(po.getCreateId());
            entity.setCreateAccount(po.getCreateAccount());
            entity.setUpdateId(po.getUpdateId());
            entity.setUpdateAccount(po.getUpdateAccount());
            entity.setCreateTime(po.getCreateTime());
            entity.setUpdateTime(po.getUpdateTime());
            apiPermissionEntityList.add(entity);
        }
        return apiPermissionEntityList;
    }

    public static PuddingApiPermissionPO toPO(AddApiPermissionDTO addApiPermissionDTO) {
        PuddingApiPermissionPO po = new PuddingApiPermissionPO();
        po.setPermName(addApiPermissionDTO.getPermName());
        po.setPermCode(addApiPermissionDTO.getPermCode());
        po.setDescription(addApiPermissionDTO.getDescription());
        po.setPermApi(addApiPermissionDTO.getPermApi());
        po.setMethod(addApiPermissionDTO.getMethod());
        po.setIsDeleted(2);
        return po;
    }

    public static PuddingApiPermissionPO toPO(PageApiPermissionListDTO pageApiPermissionListDTO) {
        PuddingApiPermissionPO po = new PuddingApiPermissionPO();
        po.setPermName(pageApiPermissionListDTO.getPermName());
        po.setPermCode(pageApiPermissionListDTO.getPermCode());
        po.setPermApi(pageApiPermissionListDTO.getPermApi());
        po.setMethod(pageApiPermissionListDTO.getMethod());
        return po;
    }

    public static PuddingApiPermissionPO toPO(AdminLoginUser loginUser, UpdateApiPermissionDTO updateApiPermissionDTO) {
        PuddingApiPermissionPO po = new PuddingApiPermissionPO();
        po.setPermName(updateApiPermissionDTO.getPermName());
        po.setPermCode(updateApiPermissionDTO.getPermCode());
        po.setPermApi(updateApiPermissionDTO.getPermApi());
        po.setMethod(updateApiPermissionDTO.getMethod());
        po.setUpdateId(loginUser.getUserId());
        po.setUpdateAccount(loginUser.getAccount());
        return po;
    }

    public static void toPO(AdminLoginUser loginUser, UpdateApiPermissionDTO updateApiPermissionDTO,PuddingApiPermissionPO po) {
        if (StrUtil.isNotEmpty(updateApiPermissionDTO.getPermName())) {
            po.setPermName(updateApiPermissionDTO.getPermName());
        }
        if (StrUtil.isNotEmpty(updateApiPermissionDTO.getPermCode())) {
            po.setPermCode(updateApiPermissionDTO.getPermCode());
        }
        if (StrUtil.isNotEmpty(updateApiPermissionDTO.getPermApi())) {
            po.setPermApi(updateApiPermissionDTO.getPermApi());
        }
        if (StrUtil.isNotEmpty(updateApiPermissionDTO.getMethod())) {
            po.setMethod(updateApiPermissionDTO.getMethod());
        }
        po.setUpdateId(loginUser.getUserId());
        po.setUpdateAccount(loginUser.getAccount());
    }


}
