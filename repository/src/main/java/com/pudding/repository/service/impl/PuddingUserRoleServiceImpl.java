package com.pudding.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pudding.repository.po.PuddingUserRolePO;
import com.pudding.repository.service.PuddingUserRoleService;
import com.pudding.repository.mapper.PuddingUserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author liuzh
* @description 针对表【pudding_user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2025-04-10 22:52:48
*/
@Service
public class PuddingUserRoleServiceImpl extends ServiceImpl<PuddingUserRoleMapper, PuddingUserRolePO>
    implements PuddingUserRoleService{


    @Override
    public PuddingUserRolePO getByUserId(Long userId) {
        LambdaQueryWrapper<PuddingUserRolePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingUserRolePO::getUserId,userId);
        return getOne(wrapper);
    }
}




