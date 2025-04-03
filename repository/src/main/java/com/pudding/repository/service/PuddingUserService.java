package com.pudding.repository.service;

import com.pudding.repository.po.PuddingUserPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author xiaoliu
* @description 针对表【pudding_user(用户表)】的数据库操作Service
* @createDate 2025-03-31 15:34:53
*/
public interface PuddingUserService extends IService<PuddingUserPO> {

    PuddingUserPO getByPhone(String phoneNumber);

    PuddingUserPO getByEmail(String email);

    PuddingUserPO getByAccount(String account);
}
