package com.pudding.repository.service;

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

}
