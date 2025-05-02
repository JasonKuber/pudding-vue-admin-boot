package com.pudding.manager.convert;

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
            apiPermissionEntityList.add(entity);
        }
        return apiPermissionEntityList;
    }

    public static PuddingApiPermissionPO toPO(PuddingApiPermissionEntity puddingApiPermissionEntity) {
        PuddingApiPermissionPO po = new PuddingApiPermissionPO();
        po.setPermName(puddingApiPermissionEntity.getPermName());
        po.setPermCode(puddingApiPermissionEntity.getPermCode());
        po.setDescription(puddingApiPermissionEntity.getDescription());
        po.setPermApi(puddingApiPermissionEntity.getPermApi());
        po.setMethod(puddingApiPermissionEntity.getMethod());
        po.setIsDeleted(2);
        po.setCreateBy(0L);
        po.setUpdateBy(0L);
        po.setId(0L);
        return po;
    }
}
