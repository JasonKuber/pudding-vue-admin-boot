package com.pudding.repository.service;

import com.pudding.repository.po.PuddingUserRolePO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liuzh
* @description 针对表【pudding_user_role(用户角色关联表)】的数据库操作Service
* @createDate 2025-04-10 22:52:48
*/
public interface PuddingUserRoleService extends IService<PuddingUserRolePO> {

    PuddingUserRolePO getByUserId(Long userId);
}
