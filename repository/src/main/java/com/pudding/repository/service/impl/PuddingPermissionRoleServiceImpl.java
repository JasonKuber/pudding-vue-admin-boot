package com.pudding.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pudding.repository.mapper.PuddingPermissionRoleMapper;
import com.pudding.repository.po.PuddingPermissionRolePO;
import com.pudding.repository.service.PuddingPermissionRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuzh
* @description 针对表【pudding_permission_role(角色权限关联表)】的数据库操作Service实现
* @createDate 2025-04-10 22:52:10
*/
@Service
public class PuddingPermissionRoleServiceImpl extends ServiceImpl<PuddingPermissionRoleMapper, PuddingPermissionRolePO>
    implements PuddingPermissionRoleService{

    @Override
    public List<PuddingPermissionRolePO> listByRoleId(Long roleId) {
        LambdaQueryWrapper<PuddingPermissionRolePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingPermissionRolePO::getRoleId,roleId);
        return list(wrapper);
    }
}




