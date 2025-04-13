package com.pudding.manager.user;

import com.pudding.domain.model.entity.PuddingUserRoleEntity;

public interface PuddingUserRoleManager {
    PuddingUserRoleEntity getUserRoleByUserId(Long id);
}
