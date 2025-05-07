package com.pudding.repository.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pudding.repository.po.PuddingApiPermissionPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liuzh
* @description 针对表【pudding_permission(权限表)】的数据库操作Service
* @createDate 2025-04-10 16:27:03
*/
public interface PuddingApiPermissionService extends IService<PuddingApiPermissionPO> {

    List<PuddingApiPermissionPO> listPermission();

    Long countApiPermissionByPermCode(String permCode);

    Long countApiPermissionByPermApi(String permApi);

    Page<PuddingApiPermissionPO> pageApiPermissionList(PuddingApiPermissionPO puddingApiPermissionPO,Integer pageNo,Integer pageSize);

    Boolean updateApiPermissionById(Long id, PuddingApiPermissionPO po);

    Boolean deleteApiPermissionById(Long userId, String account, Long id);

    Long countApiPermissionByPermApiAndMethode(String permApi, String method);
}
