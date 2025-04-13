package com.pudding.repository.service;

import com.pudding.repository.po.PuddingPermissionRolePO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liuzh
* @description 针对表【pudding_permission_role(角色权限关联表)】的数据库操作Service
* @createDate 2025-04-10 22:52:10
*/
public interface PuddingPermissionRoleService extends IService<PuddingPermissionRolePO> {

    List<PuddingPermissionRolePO> listByRoleId(Long roleId);
}
