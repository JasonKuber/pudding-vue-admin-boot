package com.pudding.manager.user.impl;

import cn.hutool.core.util.ObjectUtil;
import com.pudding.common.enums.ResultCodeEnum;
import com.pudding.common.utils.AssertUtils;
import com.pudding.domain.model.entity.PuddingUserEntity;
import com.pudding.manager.convert.PuddingUserConvert;
import com.pudding.manager.user.PuddingUserManager;
import com.pudding.repository.cache.user.PuddingUserCache;
import com.pudding.repository.po.PuddingUserPO;
import com.pudding.repository.service.PuddingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuddingUserManagerImpl implements PuddingUserManager {

    private final PuddingUserCache puddingUserCache;

    private final PuddingUserService puddingUserService;

    @Override
    public PuddingUserEntity getCacheById(String userId) {
        PuddingUserPO puddingUserPO = puddingUserCache.getCacheById(userId);
        if (ObjectUtil.isNull(puddingUserPO)) {
            puddingUserPO = puddingUserService.getById(userId);
        }
        AssertUtils.isNotNull(puddingUserPO, ResultCodeEnum.USER_INFO_ERROR);
        return PuddingUserConvert.toEntity(puddingUserPO);
    }
}
