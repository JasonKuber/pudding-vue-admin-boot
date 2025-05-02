package com.pudding.domain.model.convert;

import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;

public class PuddingApiPermissionEntityConvert {
    public static PuddingApiPermissionEntity toEntity(AddApiPermissionDTO addApiPermissionDTO) {
        PuddingApiPermissionEntity entity = new PuddingApiPermissionEntity();
        entity.setPermName(addApiPermissionDTO.getPermName());
        entity.setPermCode(addApiPermissionDTO.getPermCode());
        entity.setDescription(addApiPermissionDTO.getDescription());
        entity.setPermApi(addApiPermissionDTO.getPermApi());
        entity.setMethod(addApiPermissionDTO.getMethod());
        return entity;
    }
}
