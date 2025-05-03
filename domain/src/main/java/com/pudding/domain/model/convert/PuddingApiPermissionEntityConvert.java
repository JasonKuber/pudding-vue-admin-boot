package com.pudding.domain.model.convert;

import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.domain.model.vo.api.PageApiPermissionListVO;

import java.util.ArrayList;
import java.util.List;

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

    public static PuddingApiPermissionEntity toEntity(PageApiPermissionListDTO pageApiPermissionListDTO) {
        PuddingApiPermissionEntity entity = new PuddingApiPermissionEntity();
        entity.setPermName(pageApiPermissionListDTO.getPermName());
        entity.setPermCode(pageApiPermissionListDTO.getPermCode());
        entity.setPermApi(pageApiPermissionListDTO.getPermApi());
        entity.setMethod(pageApiPermissionListDTO.getMethod());
        return entity;
    }

    public static List<PageApiPermissionListVO> toVOList(List<PuddingApiPermissionEntity> recordList) {
        List<PageApiPermissionListVO> pageApiPermissionListVOList = new ArrayList<>();
        for (PuddingApiPermissionEntity entity : recordList) {
            PageApiPermissionListVO vo = new PageApiPermissionListVO();
            vo.setApiPermissionId(entity.getId());
            vo.setPermName(entity.getPermName());
            vo.setPermCode(entity.getPermCode());
            vo.setDescription(entity.getDescription());
            vo.setPermApi(entity.getPermApi());
            vo.setMethod(entity.getMethod());
            vo.setCreateAccount(entity.getCreateAccount());
            vo.setUpdateAccount(entity.getUpdateAccount());
            vo.setCreateTime(entity.getCreateTime());
            vo.setUpdateTime(entity.getUpdateTime());
            pageApiPermissionListVOList.add(vo);
        }
        return pageApiPermissionListVOList;
    }
}
