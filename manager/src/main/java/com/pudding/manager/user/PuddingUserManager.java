package com.pudding.manager.user;


import com.pudding.domain.model.entity.PuddingUserEntity;

public interface PuddingUserManager {
    PuddingUserEntity getCacheById(String userId);
}
