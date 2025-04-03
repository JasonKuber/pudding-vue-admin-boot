package com.pudding.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pudding.repository.po.PuddingUserPO;
import com.pudding.repository.service.PuddingUserService;
import com.pudding.repository.mapper.PuddingUserMapper;
import org.springframework.stereotype.Service;

/**
* @author xiaoliu
* @description 针对表【pudding_user(用户表)】的数据库操作Service实现
* @createDate 2025-03-31 15:34:53
*/
@Service
public class PuddingUserServiceImpl extends ServiceImpl<PuddingUserMapper, PuddingUserPO>
    implements PuddingUserService{

    @Override
    public PuddingUserPO getByPhone(String phoneNumber) {
        LambdaQueryWrapper<PuddingUserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingUserPO::getPhoneNumber,phoneNumber)
                .eq(PuddingUserPO::getIsDeleted,2);
        return getOne(wrapper);
    }

    @Override
    public PuddingUserPO getByEmail(String email) {
        LambdaQueryWrapper<PuddingUserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingUserPO::getEmail,email)
                .eq(PuddingUserPO::getIsDeleted,2);
        return getOne(wrapper);
    }

    @Override
    public PuddingUserPO getByAccount(String account) {
        LambdaQueryWrapper<PuddingUserPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PuddingUserPO::getAccount,account)
                .eq(PuddingUserPO::getIsDeleted,2);
        return getOne(wrapper);
    }
}




