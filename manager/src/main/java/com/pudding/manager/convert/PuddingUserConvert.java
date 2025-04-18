package com.pudding.manager.convert;


import com.pudding.repository.po.PuddingUserPO;
import com.pudding.domain.model.entity.PuddingUserEntity;

public class PuddingUserConvert {


    public static PuddingUserEntity toEntity(PuddingUserPO puddingUserPO) {
        PuddingUserEntity puddingUserEntity = new PuddingUserEntity();
        puddingUserEntity.setId(puddingUserPO.getId());
        puddingUserEntity.setUserName(puddingUserPO.getUserName());
        puddingUserEntity.setAccount(puddingUserPO.getAccount());
        puddingUserEntity.setPassword(puddingUserPO.getPassword());
        puddingUserEntity.setPhoneNumber(puddingUserPO.getPhoneNumber());
        puddingUserEntity.setEmail(puddingUserPO.getEmail());
        puddingUserEntity.setStatus(puddingUserEntity.enable(puddingUserPO.getStatus()));
        puddingUserEntity.setIsAdmin(puddingUserEntity.isAdmin(puddingUserPO.getIsAdmin()));
        return puddingUserEntity;
    }
}
