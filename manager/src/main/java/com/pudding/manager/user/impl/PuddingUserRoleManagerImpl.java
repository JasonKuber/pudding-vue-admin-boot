package com.pudding.manager.user.impl;

import com.pudding.domain.model.entity.PuddingUserRoleEntity;
import com.pudding.manager.user.PuddingUserRoleManager;
import com.pudding.repository.po.PuddingUserRolePO;
import com.pudding.repository.service.PuddingUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PuddingUserRoleManagerImpl implements PuddingUserRoleManager {

    private final PuddingUserRoleService puddingUserRoleService;


    @Override
    public PuddingUserRoleEntity getUserRoleByUserId(Long id) {
        PuddingUserRolePO userRolePO = puddingUserRoleService.getByUserId(id);
        return new PuddingUserRoleEntity(
                userRolePO.getUserId(),
                userRolePO.getRoleId());
    }
}
