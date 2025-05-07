package com.pudding.manager.permission.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.security.AdminLoginUser;
import com.pudding.common.utils.AssertUtils;
import com.pudding.common.vo.PageResult;
import com.pudding.domain.model.dto.api.AddApiPermissionDTO;
import com.pudding.domain.model.dto.api.PageApiPermissionListDTO;
import com.pudding.domain.model.dto.api.UpdateApiPermissionDTO;
import com.pudding.domain.model.entity.PuddingApiPermissionEntity;
import com.pudding.manager.convert.PuddingApiPermissionConvert;
import com.pudding.manager.permission.PuddingApiPermissionManager;
import com.pudding.repository.cache.permission.PuddingApiPermissionCache;
import com.pudding.repository.po.PuddingApiPermissionPO;
import com.pudding.repository.service.PuddingApiPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PuddingApiPermissionManagerImpl implements PuddingApiPermissionManager {

    private final PuddingApiPermissionService puddingApiPermissionService;

    private final PuddingApiPermissionCache puddingApiPermissionCache;


    @Override
    public List<PuddingApiPermissionEntity> listPermissionByIdList(List<Long> permIdList) {

        List<PuddingApiPermissionPO> apiPermissionPOList = puddingApiPermissionCache.getCachePermissionByIdList(permIdList);
        if (CollectionUtil.isEmpty(apiPermissionPOList)) {


            List<PuddingApiPermissionEntity> newPermissionEntityList = new ArrayList<>();
            // 初始化一下Permission列表
            List<PuddingApiPermissionEntity> permissionEntityList = allPermission();
            for (PuddingApiPermissionEntity entity : permissionEntityList) {
                if (permIdList.contains(entity.getId())) {
                    newPermissionEntityList.add(entity);
                }
            }
            return newPermissionEntityList;
        }

        return PuddingApiPermissionConvert.toEntityList(apiPermissionPOList);

    }

    @Override
    public List<PuddingApiPermissionEntity> allPermission() {

        List<PuddingApiPermissionPO> apiPermissionPOList = puddingApiPermissionCache.getCacheAllPermission();
        if (CollectionUtil.isEmpty(apiPermissionPOList)) {
            apiPermissionPOList = puddingApiPermissionService.listPermission();
            puddingApiPermissionCache.cacheAllPermission(apiPermissionPOList);
        }
        return PuddingApiPermissionConvert.toEntityList(apiPermissionPOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addApiPermission(AdminLoginUser loginUser, AddApiPermissionDTO addApiPermissionDTO) {
        Long countPermCode = puddingApiPermissionService.countApiPermissionByPermCode(addApiPermissionDTO.getPermCode());
        AssertUtils.isFalse(countPermCode > 0L, ResultCodeEnum.PARAMETERS_EXISTS,addApiPermissionDTO.getPermCode());

        Long countPermApiAndMethode = puddingApiPermissionService.countApiPermissionByPermApiAndMethode(addApiPermissionDTO.getPermApi(),addApiPermissionDTO.getMethod());
        AssertUtils.isFalse(countPermApiAndMethode > 0L, ResultCodeEnum.PARAMETERS_EXISTS,addApiPermissionDTO.getMethod() + ": " +addApiPermissionDTO.getPermApi());

        PuddingApiPermissionPO puddingApiPermissionPO = PuddingApiPermissionConvert.toPO(addApiPermissionDTO);
        puddingApiPermissionPO.setCreateId(loginUser.getUserId());
        puddingApiPermissionPO.setCreateAccount(loginUser.getAccount());
        puddingApiPermissionPO.setUpdateId(loginUser.getUserId());
        puddingApiPermissionPO.setUpdateAccount(loginUser.getAccount());
        puddingApiPermissionService.save(puddingApiPermissionPO);

        puddingApiPermissionCache.cacheApiPermission(puddingApiPermissionPO);
    }

    @Override
    public PageResult<PuddingApiPermissionEntity> pageApiPermissionList(PageApiPermissionListDTO pageApiPermissionListDTO) {
        PuddingApiPermissionPO puddingApiPermissionPO = PuddingApiPermissionConvert.toPO(pageApiPermissionListDTO);
        Page<PuddingApiPermissionPO> apiPermissionPOPage = puddingApiPermissionService.pageApiPermissionList(puddingApiPermissionPO,pageApiPermissionListDTO.getPageNo(),pageApiPermissionListDTO.getPageSize());
        List<PuddingApiPermissionEntity> apiPermissionEntityList = PuddingApiPermissionConvert.toEntityList(apiPermissionPOPage.getRecords());
        return PageResult.of(apiPermissionEntityList,apiPermissionPOPage.getTotal(),apiPermissionPOPage.getCurrent(),apiPermissionPOPage.getSize());
    }

    @Override
    public void updateApiPermission(AdminLoginUser loginUser,
                                    Long apiPermissionId,
                                    UpdateApiPermissionDTO updateApiPermissionDTO) {

        PuddingApiPermissionPO po = PuddingApiPermissionConvert.toPO(loginUser, updateApiPermissionDTO);
        Boolean updatedApiPermissionById = puddingApiPermissionService.updateApiPermissionById(apiPermissionId, po);
        AssertUtils.isTrue(updatedApiPermissionById,ResultCodeEnum.UPDATE_FAILED);


    }

    @Override
    public void deleteApiPermission(AdminLoginUser loginUser, Long apiPermissionId) {
        Boolean deleteApiPermissionById = puddingApiPermissionService.deleteApiPermissionById(loginUser.getUserId(),loginUser.getAccount(),apiPermissionId);
        AssertUtils.isTrue(deleteApiPermissionById,ResultCodeEnum.DELETE_FAILED);
    }
}
