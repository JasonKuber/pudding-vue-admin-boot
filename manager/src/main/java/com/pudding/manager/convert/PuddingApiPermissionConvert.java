package com.pudding.manager.convert;

import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.domain.model.vo.api.PageApiPermissionListVO;
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


    public static List<PageApiPermissionListVO> toVOList(List<PuddingApiPermissionEntity> entityList) {
        return null;
    }
}
